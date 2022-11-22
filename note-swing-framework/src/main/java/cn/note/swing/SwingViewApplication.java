package cn.note.swing;

import cn.note.service.toolkit.filestore.SystemFileManager;
import cn.note.swing.core.util.SwingCoreUtil;
import cn.note.swing.core.view.filechooser.FileChooserBuilder;
import cn.note.swing.core.view.frame.DestroyHandleFrame;
import cn.note.swing.core.view.icon.SvgIconFactory;
import cn.note.swing.core.view.loading.SpinLoading;
import cn.note.swing.core.view.panel.ToggleLRCardPanel;
import cn.note.swing.core.view.theme.ThemeFlatLaf;
import cn.note.swing.toolkit.ToolkitView;
import cn.note.swing.view.note.NoteEditorView;
import cn.note.swing.view.qacard.QACardView;
import lombok.extern.slf4j.Slf4j;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;

import javax.swing.*;
import java.awt.*;

/**
 * @author jee
 * 自定义jframe创建swing视图
 */
@Slf4j
public class SwingViewApplication extends SingleFrameApplication {

    private static JFrame frame;

    private JPanel container;

    public static void main(String[] args) throws Exception {
//        LoggerUtil.setLogLevelInfo(SwingViewApplication.class);
        launch(SwingViewApplication.class, args);


    }

    @Override
    public FrameView getMainView() {
        FrameView frameView = new FrameView(this);
        DestroyHandleFrame destroyHandleFrame = new DestroyHandleFrame();
        destroyHandleFrame.setMinimumSize(new Dimension(1024, 600));
        ResourceMap resourceMap = this.getContext().getResourceMap();
        String title = resourceMap.getString("Application.title");
        destroyHandleFrame.setTitle(title);
        if (resourceMap.containsKey("Application.icon")) {
            Image icon = resourceMap.getImageIcon("Application.icon").getImage();
            destroyHandleFrame.setIconImage(icon);
        }
        frameView.setFrame(destroyHandleFrame);
        return frameView;
    }

    @Override
    protected void startup() {
        ThemeFlatLaf.install();
        frame = getMainFrame();
        show(frame);
        // D:/note-helper
        SystemFileManager.updateSystemDir2Default();

        ToggleLRCardPanel toggleLRCardPanel = new ToggleLRCardPanel();
        toggleLRCardPanel.addTab(SvgIconFactory.Note.editor, "笔记管理", new NoteEditorView(true), true);
        toggleLRCardPanel.addTab(SvgIconFactory.Note.question, "问答管理", new QACardView(true));
        toggleLRCardPanel.addTab(SvgIconFactory.Note.plugins, "插件管理", new ToolkitView(true));
        SpinLoading<ToggleLRCardPanel> mainLoading = new SpinLoading<>(toggleLRCardPanel, 500);
        frame.add(mainLoading);

    }

    @Override
    protected void configureWindow(Window root) {
        super.configureWindow(root);
        if (root == getMainFrame()) {
            root.pack();
            root.setBounds(SwingCoreUtil.centerScreenRectangle(1D, 1D));
            root.validate();
        }
    }


    @Override
    protected void ready() {

        log.info("系统加载完成!");
        // 预加载单例文件夹选择器
        FileChooserBuilder.install();
    }

}
