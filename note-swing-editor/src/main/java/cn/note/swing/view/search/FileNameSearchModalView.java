package cn.note.swing.view.search;

import cn.hutool.core.util.StrUtil;
import cn.note.swing.core.component.ComponentMover;
import cn.note.swing.core.event.change.ChangeDocumentListener;
import cn.note.swing.core.lifecycle.InitAction;
import cn.note.swing.core.view.AbstractMigView;
import cn.note.swing.core.view.modal.ModalFactory;
import cn.note.swing.core.view.theme.ThemeColor;
import cn.note.swing.store.NoteContext;
import cn.note.swing.store.NoteIndexManager;
import cn.note.swing.view.note.tab.NoteTabView;
import com.formdev.flatlaf.ui.FlatEmptyBorder;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingx.JXTitledPanel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 文件搜搜模态框
 */
public class FileNameSearchModalView {

    private NoteContext noteContext;

    /*搜多模态框*/
    @Getter
    private JDialog dialog;

    /*索引管理器*/
    private NoteIndexManager noteIndexManager;

    /*选项卡视图*/
    private NoteTabView noteTabView;

    /*窗口*/
    private Window window;

    public FileNameSearchModalView() {
        this.noteContext = NoteContext.getInstance();
        this.noteIndexManager = noteContext.getNoteIndexManager();
        this.noteTabView = noteContext.getNoteTabView();
        this.window = noteContext.getActiveWindow();
        this.init();
    }

    private void init() {
        dialog = ModalFactory.dialogModal((JFrame) window, 0.6D, 0.6D);
        dialog.setContentPane(new FileSearchView(this.noteIndexManager.getFileContextIndexManager()));
        ComponentMover cm = new ComponentMover();
        cm.registerComponent(dialog);

        // frame失去焦点 时隐藏窗口
        window.addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                dialog.setVisible(false);
            }
        });

    }

    /**
     * 搜索视图
     */
    private class FileSearchView extends AbstractMigView {

        /*搜索狂*/
        private JTextField search;

        /*搜索面板*/
        private JList<FileContext> fileContextJList;

        /* 滚动面板*/
        private JScrollPane fileListScroll;

        /*行容器*/
        private JPanel row;

        /* 文件管理器*/
        private FileContextIndexManager fileContextIndexManager;

        public FileSearchView(FileContextIndexManager fileContextIndexManager) {
            super(true);
            this.fileContextIndexManager = fileContextIndexManager;
            super.display();
        }


        @Override
        protected MigLayout defineMigLayout() {
            return new MigLayout("wrap 1,gap 0,insets 0", "[grow]", "[grow]");
        }

        @Override
        protected void init() {
            DefaultListModel<FileContext> listModel = new DefaultListModel<>();
            fileContextJList = new JList<>(listModel);
            search = new JTextField();
            fileListScroll = new JScrollPane(fileContextJList);
            SearchListStyleWrapper.addSearchMoveListStyle(dialog, fileListScroll, search, fileContextJList, this::openSelectedFile);
            fileContextJList.setCellRenderer(new FileNameSearchRender());
            fileListScroll.setVisible(false);

        }

        /**
         * render视图
         */
        @Override
        protected void render() {
            row = new JPanel(new MigLayout("wrap 1,gap 0,insets 0"));
            row.setBackground(ThemeColor.themeColor);
            row.add(search, "w 400:100%:,h 35!");
            row.add(fileListScroll, "grow,h :300:500,,gapleft 2,gapright 2");
            fileListScroll.setVisible(false);
            SearchListStyleWrapper.createScrollStyle(fileListScroll);
            JXTitledPanel titledPanel = SearchListStyleWrapper.createTitledPanel("Search File", row);
            titledPanel.setBorder(new FlatEmptyBorder());
            view.add(titledPanel, "grow");
        }


        @InitAction("搜索框实现动态改变JList")
        private void bindSearch() {
            // 当输入内容变化时执行搜索
            search.getDocument().addDocumentListener(new ChangeDocumentListener() {
                @Override
                public void update(DocumentEvent e) {
                    doSearch();
                }
            });

            // 当窗口显示时执行搜索
            dialog.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentShown(ComponentEvent e) {
                    fileContextIndexManager.setSearchType(SearchType.FileName);
                    SearchListStyleWrapper.oldSelectedIndex = 0;
                    doSearch();
                }
            });

        }

        /**
         * 根据输入框内容搜索
         */
        private void doSearch() {

            String searchText = search.getText();
            if (StrUtil.isBlank(searchText)) {
                fileListScroll.setVisible(false);
            } else {
                DefaultListModel<FileContext> searchData = new DefaultListModel<>();
                // 格式化搜索内容
                fileContextIndexManager.searchIndex(searchText).forEach(searchData::addElement);
                fileContextJList.setModel(searchData);
                if (searchData.size() > 0) {
                    fileContextJList.setSelectedIndex(0);
                }
                fileListScroll.setVisible(true);
            }
            view.revalidate();

        }


        private void openSelectedFile() {
            int searchIndex = fileContextJList.getSelectedIndex();
            if (searchIndex > -1) {
                FileContext fileContext = fileContextJList.getModel().getElementAt(searchIndex);
                noteTabView.editNote(fileContextIndexManager.indexToFileObject(fileContext));
                dialog.setVisible(false);
            }
        }

    }

}
