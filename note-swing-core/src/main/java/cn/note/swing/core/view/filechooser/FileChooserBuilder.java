package cn.note.swing.core.view.filechooser;

import cn.note.swing.core.view.wrapper.TextFieldWrapper;
import cn.note.swing.core.view.icon.SvgIconFactory;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.function.Consumer;

/**
 * 快速创建文件选择器
 */
public class FileChooserBuilder {

    private static File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();

    //    默认的JFileChooser
    private static FutureTask<JFileChooser> futureFileChooser = new FutureTask<>(JFileChooser::new);
//    private static FutureTask<JFileChooser> futureFileChooser = new FutureTask<>(NativeJFileChooser::new);

    private static JFileChooser shareFileChooser = null;


    public static void install() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(futureFileChooser);
    }

    private static void createChooser() {
        try {
            if (shareFileChooser == null) {
                shareFileChooser = futureFileChooser.get();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JFileChooser createFileChooser() {
        createChooser();
        shareFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        shareFileChooser.setMultiSelectionEnabled(false);
        shareFileChooser.setCurrentDirectory(desktopDir);
        return shareFileChooser;
    }


    public static JFileChooser createDirChooser() {
        createChooser();
        shareFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        shareFileChooser.setMultiSelectionEnabled(false);
        shareFileChooser.setCurrentDirectory(desktopDir);
        return shareFileChooser;
    }


    /**
     * file chooser 使用示例
     *
     * @param fileChooser    文件选择器
     * @param type           操作类型
     * @param selectConsumer 确认回调
     * @param cancel         取消回调
     */
    public static void showFileChooser(JFileChooser fileChooser, int type, Consumer<File> selectConsumer, Runnable cancel) {
        int result;
        // 打开 or 保存
        if (type == JFileChooser.OPEN_DIALOG) {
            result = fileChooser.showOpenDialog(null);
        } else if (type == JFileChooser.SAVE_DIALOG) {
            result = fileChooser.showSaveDialog(null);
        } else {
            result = fileChooser.showDialog(null, "确定");
        }

        // 确定回调
        if (result == JFileChooser.APPROVE_OPTION) {
            if (selectConsumer != null) {
                // 回调选择文件
                File selectFile = fileChooser.getSelectedFile();
                selectConsumer.accept(selectFile);
            }
        } else {
            if (cancel != null) {
                cancel.run();
            }
        }

        // 重置
        fileChooser.setSelectedFile(new File(""));
    }


    public static JTextField inputFileChooser(String title) {
        FlatSVGIcon icon = SvgIconFactory.icon(SvgIconFactory.Common.addFile);
        return inputFileChooser(title, new JButton(icon));
    }

    public static JTextField inputFileChooser(String title, JButton suffixBtn) {
        return inputChooser(FileChooserType.File, title, suffixBtn);
    }

    public static JTextField inputDirChooser(String title) {
        FlatSVGIcon icon = SvgIconFactory.icon(SvgIconFactory.Common.folder);
        return inputDirChooser(title, new JButton(icon));
    }

    public static JTextField inputDirChooser(String title, JButton suffixBtn) {
        return inputChooser(FileChooserType.Directory, title, suffixBtn);
    }


    public static JTextField inputChooser(FileChooserType chooserType, String title, JButton suffixBtn) {
        // 文件夹选择器
        JTextField inputFileChooser = TextFieldWrapper.create()
                .suffix(suffixBtn)
                .showClear().build();
        //选择事件
        suffixBtn.addActionListener(e -> {
            // 后台进程在创建fileChooser
            switch (chooserType) {
                case File:
                    createFileChooser();
                    break;
                case Directory:
                    createDirChooser();
                    break;
            }
            shareFileChooser.setDialogTitle(title);
            Window win = SwingUtilities.getWindowAncestor(suffixBtn);
            int resultValue = shareFileChooser.showOpenDialog(win);
            if (resultValue == JFileChooser.APPROVE_OPTION) {
                inputFileChooser.setText(shareFileChooser.getSelectedFile().getAbsolutePath());
            }
            shareFileChooser.setSelectedFile(new File(""));
        });

        // 输入框
        return inputFileChooser;
    }


}
