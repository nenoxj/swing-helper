package cn.note.swing.view.note.editor.core.richtext.core;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.note.swing.core.event.key.RegisterKeyAction;
import cn.note.swing.view.note.editor.core.richtext.handler.action.EditorAction;
import cn.note.swing.view.note.editor.core.richtext.handler.action.EnhanceActionHandler;
import cn.note.swing.view.note.editor.core.richtext.handler.caret.CaretColorHandler;
import cn.note.swing.view.note.editor.core.richtext.handler.hyperlink.HyperlinkMouseHandler;
import cn.note.swing.view.note.editor.core.richtext.handler.image.EditorCacheImage;
import cn.note.swing.view.note.editor.core.richtext.handler.image.ImageSupport;
import cn.note.swing.view.note.editor.core.richtext.handler.keyboard.EnterTwiceHandler;
import cn.note.swing.view.note.editor.core.richtext.handler.search.SearchHandler;
import cn.note.swing.view.note.editor.core.richtext.handler.source.HtmlSourceHandler;
import cn.note.swing.view.note.editor.core.richtext.handler.style.*;
import cn.note.swing.view.note.editor.core.richtext.handler.undo.UndoActionHandler;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.jdesktop.swingx.search.SearchFactory;
import org.jdesktop.swingx.search.Searchable;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.html.HTML;
import javax.swing.text.html.StyleSheet;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.io.IOException;

/**
 * 富文本编辑器
 * <p>
 * 1. 记录source
 * 2. 粘贴html首先插入一行,再粘贴. 可解决很多问题
 * 3. 如果操作的元素为pre/div等, 判断enter 2次中断标签,进入新行
 * 4. 粘贴图片时,先存入本地缓存,然后通过paste html处理
 * 5. 光标事件 在深色和浅色区域变化时自动换色
 * 6. 鼠标事件 监听超链接本地浏览器打开
 * 7. 键盘事件 绑定常用格式化操作
 * 8. dom事件  查看文本源码
 * 9. 撤销和恢复事件
 * <p>
 * --- issue copy idea文本编辑器内容至编辑器时
 * -- -当文字没有被标签包裹时, 会发生问题 ,应该在父标签指定样式
 * <pre style="background-color: #000000">
 *     extendedEditor
 * 	<span>
 * 		<font color="#f92772">
 * 			public
 * 		</font>
 * 	</span>
 * </pre>
 */
@Slf4j
public class ExtendedEditor extends JTextPane {
//public class ExtendedEditor extends JEditorPane {

    /**
     * 源码拦截器
     */
    private HtmlSourceHandler htmlSourceHandler;

    /**
     * 搜索功能
     */
    private SearchHandler searchHandler;
    /**
     * html document
     */
    private ExtendedHTMLDocument htmlDoc;

    /**
     * 保存图像实现接口
     */
    @Getter
    @Setter
    private ImageSupport imageSupport;

    /**
     * 是否允许粘贴html
     */
    @Getter
    @Setter
    private boolean allowPasteHtml;

    /* 撤销/恢复拦截器*/
    private UndoActionHandler undoActionHandler;


    public ExtendedEditor() {
        ExtendedHTMLEditorKit htmlKit = new ExtendedHTMLEditorKit();
        htmlDoc = (ExtendedHTMLDocument) htmlKit.createDefaultDocument();
        this.setEditorKit(htmlKit);
        this.setDocument(htmlDoc);
        this.setMargin(new Insets(10, 10, 10, 10));
        initHandlers();
        // 控制背景透明
        StyleSheet styleSheet = htmlKit.getStyleSheet();
        Style style = styleSheet.getStyle("body");
        StyleConstants.setBackground(style, new Color(0, 0, 0, 0));

    }


    /**
     * 初始化拦截器
     */
    private void initHandlers() {
        // 样式事件注册
        new GlobalStyleActionHandler(this);
        // 源码事件
        htmlSourceHandler = new HtmlSourceHandler(this);
        // 图像支持
        imageSupport = new EditorCacheImage(this);
        //拓展事件优先注册
        EnhanceActionHandler extendedAction = new EnhanceActionHandler(this);
        undoActionHandler = new UndoActionHandler(this);
        addAllRegisterKeyActions(extendedAction, undoActionHandler);
        ThreadUtil.execute(() -> {
            // 光标事件拦截 CaretColorHandler caretHandler =
            new CaretColorHandler(this);
            // 键盘事件拦截 EnterTwiceHandler keyboardHandler =
            new EnterTwiceHandler(this);
            //超链接事件拦截MouseHandler mouseHandler =
            new HyperlinkMouseHandler(this);
            // 取消插入filter拦截 DocumentHandler  documentHandler =
//            new DocumentHandler(this);
            RegisterKeyAction fontStyleAction = new FontStyleActionHandler(this);
            RegisterKeyAction preStyleAction = new PreCodeStyleActionHandler(this);
            RegisterKeyAction pStyleAction = new PStyleActionHandler(this);
            RegisterKeyAction imageStyleAction = new ImageStyleActionHandler(this);
            addAllRegisterKeyActions(fontStyleAction, preStyleAction, pStyleAction, imageStyleAction);
        });
    }

    /**
     * @return 获取html资源编辑器
     */
    public RSyntaxTextArea getSourceEditor() {
        return htmlSourceHandler.getHtmlSource();
    }


    /**
     * @return 当前编辑器是否发生变化
     */
    public boolean isChanged() {
        return htmlSourceHandler.isChanged();
    }

    /**
     * 设置编辑器状态变化为false
     */
    public void completeChanged() {
        htmlSourceHandler.setChanged(false);
    }

    /**
     * 粘贴图片进行特殊操作
     */
    @Override
    public void paste() {
        try {
            // 获取剪切板类型
            Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);

            // 粘贴html 未验证是否可以
            if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.allHtmlFlavor)) {
                if (isAllowPasteHtml()) {
                    super.paste();
                    // 插入新行
                    this.callEditorKitAction(EditorAction.INSERT_P);
                    this.refreshText();
                } else {
                    pasteHtmlAsPlainText();
                }
            } else if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.imageFlavor)) {
                // 粘贴图片时 调用ImageSupport 实现拦截处理
                callEditorKitAction(EditorAction.INSERT_IMG_FROM_CLIP);
            } else {
                super.paste();
            }
        } finally {
            // 在开启粘贴html功能时,允许使用一次,使用完成后,设置状态为false
            setAllowPasteHtml(false);
        }

    }

    /**
     * 粘贴html时 作为文本解析
     */
    private void pasteHtmlAsPlainText() {
        try {
            // 选中文本时 先删除再插入
            if (StrUtil.isNotBlank(this.getSelectedText())) {
                int start = this.getSelectionStart();
                int end = this.getSelectionEnd();
                this.getDocument().remove(start, end);
                setCaretPosition(start);
            }
            this.getDocument().insertString(this.getCaretPosition(), Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor).toString(), null);
        } catch (BadLocationException | UnsupportedFlavorException | IOException e) {
            log.error("粘贴文本error!", e);
        }
    }


    /**
     * 插入一个新行, 并回到原位
     */
    public void insertNewLine() {
        int pos = this.getCaretPosition();
        callEditorKitAction(DefaultEditorKit.insertBreakAction);
        this.setCaretPosition(pos);
    }


    /**
     * @return 光标是否在末尾
     */
    public boolean isLastCaret() {
        return this.getCaretPosition() == this.getDocument().getLength();
    }


    /**
     * @return 获取行数量
     */
    public int getLineCount() {
        int totalCharacters = this.getText().length();
        int lineCount = (totalCharacters == 0) ? 1 : 0;

        try {
            int offset = totalCharacters;
            while (offset > 0) {
                offset = Utilities.getRowStart(this, offset) - 1;
                lineCount++;
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        return lineCount;
    }


    /**
     * 是否为第一行
     */
    public boolean isFirstLine() {
        int pos = getCaretPosition();
        if (pos == 0) {
            return true;
        }
        moveCaretLineBegin();
        int movePos = getCaretPosition();
        if (movePos == 0) {
            setCaretPosition(pos);
            return true;
        }
        return false;
    }

    /**
     * @return 是否最后一行
     */
    public boolean isLastLine() {
        int pos = getCaretPosition();
        int lastPos = this.getDocument().getLength();
        if (pos == lastPos) {
            return true;
        }
        moveCaretLineEnd();
        int movePos = getCaretPosition();
        if (movePos == lastPos) {
            setCaretPosition(pos);
            return true;
        }
        return false;
    }

    /**
     * 选择行
     */
    public void selectLine() {
        callEditorKitAction(DefaultEditorKit.selectLineAction);
    }

    /**
     * 删除行
     */
    public void deleteLine() {
        callEditorKitAction(EditorAction.DELETE_LINE.getName());
    }

    /**
     * 移动光标至行尾
     */
    public void moveCaretLineEnd() {
        callEditorKitAction(DefaultEditorKit.endLineAction);
    }

    /**
     * 移动光标至行首
     */
    public void moveCaretLineBegin() {
        callEditorKitAction(DefaultEditorKit.beginLineAction);
    }

    /**
     * 移动光标至上一行
     */
    public void moveCaretPrevLine() {
        moveCaretLineBegin();
        int pos = getCaretPosition();
        setCaretPosition(pos > 0 ? pos - 1 : pos);
    }

    /**
     * 移动光标至下一行
     */
    public void moveCaretNextLine() {
        moveCaretLineEnd();
        int pos = getCaretPosition();
        setCaretPosition(isLastCaret() ? pos : pos + 1);
    }

    /**
     * 调用DefaultEditorKit默认action
     *
     * @param actionName 动作名称
     * @see DefaultEditorKit
     */
    public void callEditorKitAction(String actionName) {
        this.getActionMap().get(actionName).actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));

    }

    /**
     * @param editorAction 调用EditorAction 注册action
     */
    public void callEditorKitAction(EditorAction editorAction) {
        callEditorKitAction(editorAction.getName());
    }

    /**
     * 默认的查找面板
     */
    public void find() {
        SearchFactory.getInstance().showFindInput(this, this.getSearchable());
    }

    /**
     * 如果未实现searchable 则使用jxswing 包的默认Searchable
     *
     * @return 搜索实现类
     */
    public Searchable getSearchable() {
        if (this.searchHandler == null) {
            this.searchHandler = new SearchHandler(this);
        }
        return this.searchHandler;
    }


    /**
     * 添加动作注册
     */
    private void addAllRegisterKeyActions(RegisterKeyAction... registerKeyActions) {
        for (RegisterKeyAction registerKeyAction : registerKeyActions) {
            registerKeyAction.addKeyActions();
        }
    }


    /**
     * @param tag HTML标签
     * @return 是否为某html标签
     */
    public boolean isElement(HTML.Tag tag) {
        int caretPos = this.getCaretPosition();
        Element element = Utilities.getParagraphElement(this, caretPos);
        if (element != null) {
            return element.getName().equalsIgnoreCase(tag.toString());
        }
        return false;
    }

    /**
     * @param tag HTML标签
     * @return 父元素是否为某html标签
     */
    public boolean isParentElement(HTML.Tag tag) {
        int caretPos = this.getCaretPosition();
        Element element = Utilities.getParagraphElement(this, caretPos);
        if (element != null) {
            String eName = element.getParentElement().getName();
            return eName.equalsIgnoreCase(tag.toString());

        }
        return false;
    }


    /**
     * @return 下一个元素是否为pre元素
     */
    public boolean isNextPreElement() {
        moveCaretNextLine();
        boolean result = isParentElement(HTML.Tag.PRE);
        moveCaretPrevLine();
        return result;
    }

    /**
     * @return 上一个元素是否为pre元素
     */
    public boolean isPrevPreElement() {
        moveCaretPrevLine();
        boolean result = isParentElement(HTML.Tag.PRE);
        moveCaretNextLine();
        return result;
    }

    /**
     * 插入文本
     *
     * @param text 文本内容
     */
    public void insertStr(String text) {
        int pos = this.getCaretPosition();
        try {
            htmlDoc.insertString(pos, text, null);
        } catch (BadLocationException e) {
            log.error("插入文本error: {}", e.getMessage());
        }
    }

    /**
     * 控制行移动来调用方法
     *
     * @param runnable 回调方法
     */
    public void batchLineRun(Runnable runnable) {
        htmlDoc.controlUndo(0, htmlDoc.getLength(), () -> {
            int caretPos = getCaretPosition();
            // 处理select
            int selectStart = getSelectionStart();
            int selectEnd = getSelectionEnd();
            if (selectStart > selectEnd) {
                int tmp = selectEnd;
                selectEnd = selectStart;
                selectStart = tmp;
            }
            setCaretPosition(selectEnd);
            // 依赖光标移动实现
            int cursor = selectEnd;
            while (cursor >= selectStart) {
                runnable.run();
                moveCaretPrevLine();
                cursor = getCaretPosition();
                if (cursor == 0) {
                    break;
                }
            }
            try {
                setCaretPosition(caretPos);
            } catch (Exception e) {
                log.warn("无法重置光标位置!");
            }
        });
        int caretPos = this.getCaretPosition();
        this.setText(this.getText());
        this.repaint();
        this.setText(this.getText());
        this.setCaretPosition(caretPos);
    }


    /*----------------------------未验证功能---------------------------------------*/

    public void refreshOnUpdate() {
        this.setText(this.getText());
        getSourceEditor().setText(this.getText());
        this.setText(getSourceEditor().getText());
        try {
            this.setCaretPosition(this.getDocument().getLength());
        } catch (IllegalArgumentException iea) { /* caret position bad, probably follows a deletion */ }
        this.repaint();
    }

    public void refresh() {
        undoActionHandler.refresh();
        this.repaint();
        this.setCaretPosition(0);
    }


    public void refreshText() {
        //TODO 应该解析文本是否存在内容,否则设置空字符串
        String text = this.getText();
        this.setText(text);
        this.repaint();
        this.setText(text);
    }

    public StyledDocument getStyledDocument() {
        return htmlDoc;
    }
}

