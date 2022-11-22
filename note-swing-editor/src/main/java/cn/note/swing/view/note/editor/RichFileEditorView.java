package cn.note.swing.view.note.editor;

import cn.hutool.core.util.StrUtil;
import cn.note.service.toolkit.filestore.DiskFileStore;
import cn.note.service.toolkit.filestore.RelativeFileStore;
import cn.note.swing.core.event.ConsumerAction;
import cn.note.swing.core.event.focus.RequestFocusListener;
import cn.note.swing.core.event.key.KeyActionFactory;
import cn.note.swing.core.lifecycle.InitAction;
import cn.note.swing.core.view.AbstractMigView;
import cn.note.swing.core.view.wrapper.FlatWrapper;
import cn.note.swing.core.view.theme.ThemeColor;
import cn.note.swing.store.NoteConstants;
import cn.note.swing.view.note.editor.core.richtext.core.ExtendedEditor;
import cn.note.swing.view.note.editor.core.richtext.core.ExtendedHTMLEditorKit;
import cn.note.swing.view.note.editor.core.richtext.handler.action.EditorAction;
import cn.note.swing.view.note.editor.core.richtext.handler.image.EditorRelativeFileImage;
import cn.note.swing.view.note.editor.core.richtext.handler.search.FindReplaceView;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.action.AbstractActionExt;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * 富文本测试
 */
@Slf4j
public class RichFileEditorView extends AbstractMigView {
    /**
     * html编辑器
     */
    @Getter
    private ExtendedEditor htmlEditor;

    /* 文本编辑器面板*/
    private JScrollPane textPane;

    /*层级视图*/
    private JLayeredPane layerView;

    /*查找*/
    private JXCollapsiblePane findCollapse;
    /**
     * 磁盘存储
     */
    private DiskFileStore diskFileStore;

    /**
     * 笔记存储
     */
    private RelativeFileStore noteFileStore;

    /* 查找工具栏*/
    private FindReplaceView findBar;

    /* 文件内容*/
    private String fileContext;

    public RichFileEditorView(RelativeFileStore noteFileStore, File editFile) {
        super(true);
        this.diskFileStore = new DiskFileStore(editFile);
        this.noteFileStore = noteFileStore;
        super.display();
    }


    /*更新绑定文件*/
    public void updateDiskFileStore(File newFile) {
        this.diskFileStore = new DiskFileStore(newFile);
    }


    @Override
    protected MigLayout defineMigLayout() {
        return new MigLayout("insets 0,gap 0,wrap 1", "[grow]", "[grow]");
    }

    @Override
    protected void init() {
        htmlEditor = new ExtendedEditor();

        // 设置图片相对存储目录
        EditorRelativeFileImage relativeFileImage = new EditorRelativeFileImage(htmlEditor, noteFileStore.getRelativeFileStore(NoteConstants.IMAGE_FOLDER));
        htmlEditor.setImageSupport(relativeFileImage);

        JPanel disableLineWrap = new JPanel(new BorderLayout());
        disableLineWrap.add(htmlEditor);
        textPane = new JScrollPane(disableLineWrap);
        FlatWrapper.decorativeScrollPane(textPane, ThemeColor.fontColor, ThemeColor.grayColor);
        textPane.setBorder(BorderFactory.createEmptyBorder());
        try {
            // 读取内容
            fileContext = diskFileStore.read();
        } catch (IOException e) {
            log.error("加载内容失败", e);
            super.refuse("文件损坏,无法加载内容");
        }


    }

    @Override
    protected void render() {
        // 注册右键菜单
        this.getHtmlEditor().addMouseListener(new RichFileEditorPopupView(getHtmlEditor()));

        findCollapse = defineFindBarCollapsible();
        layerView = new JLayeredPane();
        layerView.setLayout(new MigLayout("insets 0,gap 0", "[grow]", "[grow]"));
        layerView.setLayer(findCollapse, JLayeredPane.MODAL_LAYER);
        layerView.add(textPane, "w 100%,h 100%");
        layerView.add(findCollapse, "w 450,pos 1al 0");
        view.add(layerView, "w 100%,h 100%");

    }

    @InitAction("初始化默认焦点")
    private void requestInitFocus() {


        // 指定htmlEditor 初次加载时 默认获取焦点
        htmlEditor.addHierarchyListener(new RequestFocusListener());

        // 指定组件渲染完成后,再设置内容
        this.addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                htmlEditor.setText(fileContext);
                RichFileEditorView.this.removeAncestorListener(this);
            }

            @Override
            public void ancestorRemoved(AncestorEvent event) {

            }

            @Override
            public void ancestorMoved(AncestorEvent event) {

            }
        });
    }


    /**
     * 让编辑器请求焦点
     */
    public void focus2Editor() {
        htmlEditor.requestFocusInWindow();
    }

    /**
     * 可折叠的查找/替换面板
     * Color.decode("#FF6666");
     */
    protected JXCollapsiblePane defineFindBarCollapsible() {
        final JXCollapsiblePane collapsible = new JXCollapsiblePane(JXCollapsiblePane.Direction.DOWN);
        collapsible.setOpaque(false);
        findBar = new FindReplaceView(htmlEditor);
        collapsible.add(findBar);
        collapsible.setCollapsed(true);
        collapsible.setAnimated(false);

        Action openFindBar = new AbstractActionExt() {
            private static final long serialVersionUID = -4709060878474397796L;

            @Override
            public void actionPerformed(ActionEvent e) {
                collapsible.setCollapsed(false);
//                KeyboardFocusManager.getCurrentKeyboardFocusManager()
//                        .focusNextComponent(findBar);
                findBar.showSearchText(true);
            }
        };
        Action closeFindBar = new AbstractActionExt() {
            private static final long serialVersionUID = 3824587538699271342L;

            @Override
            public void actionPerformed(ActionEvent e) {
                collapsible.setCollapsed(true);
                findBar.showSearchText(false);
                htmlEditor.requestFocusInWindow();
            }

        };
        htmlEditor.getActionMap().put("find", openFindBar);
        findBar.getActionMap().put("close", closeFindBar);
        return collapsible;
    }

    /**
     * 注册ctrl+s 手动保存
     */
    @InitAction
    private void registerKeySave() {
        KeyActionFactory.registerFocusKeyAction(htmlEditor, EditorAction.SAVE, new ConsumerAction((e) -> save()));


        // 按键ctrl h 取消删除字符
        KeyActionFactory.registerFocusKeyAction(htmlEditor, EditorAction.DEFAULT_CTRL_H, new ConsumerAction(e -> {
        }));

    }


    /**
     * 同步编辑器内容至文件
     */
    public void save() {
        try {
            if (htmlEditor.isChanged()) {
//                writeFileByHtmlKit();
                writeFileByJsoup();
            }
        } catch (IOException e) {
            log.error("无法保存文件", e);
        }
    }


    private void writeFileByHtmlKit() throws IOException {
        ExtendedHTMLEditorKit htmlEditorKit = (ExtendedHTMLEditorKit) htmlEditor.getEditorKit();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            htmlEditorKit.write(baos, htmlEditor.getStyledDocument(), 0, htmlEditor.getStyledDocument().getLength());
        } catch (BadLocationException e) {
            log.error("写入文件异常", e);
        }
        diskFileStore.write(baos);
        htmlEditor.completeChanged();
    }


    private void writeFileByJsoup() throws IOException {
        // 使用jsoup 对空格进行特殊处理
        String htmlText = htmlEditor.getText();
        Document document = Jsoup.parse(htmlText);
        document.outputSettings(new Document.OutputSettings().prettyPrint(false));
        document.select("p").forEach(element ->
        {
            String text = element.text();
            String html = element.html();
            String startSep = "      ";
            String endSep = "    ";
            // 移除前后换行
            html = html.replace("\r\n", "");

            // 移除p标签的前置内容
            if (html.indexOf(startSep) == 0) {
                html = StrUtil.sub(html, startSep.length(), html.length());
            }
            // 移除p标签的后置内容
            html = StrUtil.subBefore(html, endSep, true);

            String newHtml = html.replace(" ", "&nbsp;");
            element.html(newHtml);
        });
        // 空格做特殊处理 --这样出现的问题多出换行
//                text = text.replace("  ","&nbsp;").replace("\t", "&#09;");
//                diskFileStore.write(htmlText);
        diskFileStore.write(document.html());
        htmlEditor.completeChanged();

    }

}

