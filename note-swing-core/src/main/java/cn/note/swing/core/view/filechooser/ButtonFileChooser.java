package cn.note.swing.core.view.filechooser;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.function.Consumer;

/**
 * 按钮文件选择器
 * <p>
 * 默认创建 保存文件的选择器,可通过set方法重置
 * <p>
 * // ------------旧的使用方式 --------------
 * JButton  exportFile=new JButton("保存文件");
 * exportFile.addActionListener((e) -> {
 * int returnValue = saveImageChooser.showSaveDialog(this);
 * if (JFileChooser.APPROVE_OPTION == returnValue) {
 * File saveFile = saveImageChooser.getSelectedFile();
 * String fileName = saveFile.getName();
 * if (!fileName.endsWith(".png")) {
 * saveFile = Paths.get(saveFile.getParent(), fileName + ".png").toFile();
 * }
 * try {
 * BufferedImage bi = SwingCoreUtil.exportComp2Image(bgArea);
 * ImageIO.write(bi, "PNG", saveFile);
 * saveImageChooser.setSelectedFile(new File(""));
 * MessageBuilder.ok(this, "保存成功!");
 * } catch (IOException e1) {
 * e1.printStackTrace();
 * }
 * }
 * });
 * <p>
 * // ---------使用ButtonFileChooser-----------
 * <p>
 * JButton exportFile = new ButtonFileChooser("保存图片", saveFile -> {
 * String fileName = saveFile.getName();
 * if (!fileName.endsWith(".png")) {
 * saveFile = Paths.get(saveFile.getParent(), fileName + ".png").toFile();
 * }
 * try {
 * BufferedImage bi = SwingCoreUtil.exportComp2Image(bgArea);
 * ImageIO.write(bi, "PNG", saveFile);
 * saveImageChooser.setSelectedFile(new File(""));
 * MessageBuilder.ok(this, "保存成功!");
 * } catch (IOException e) {
 * log.error("保存图片失败!", e);
 * }
 * });
 * <p>
 * ButtonFactory.decorativeButton(exportFile, ThemeColor.primaryColor);
 */
public class ButtonFileChooser extends JButton {


    private JFileChooser fileChooser;

    private Consumer<File> saveConsumer;

    private FileChooserType fileChooserType;


    public ButtonFileChooser(String text, Consumer<File> onSaveFile) {
        super(text);
        this.saveConsumer = onSaveFile;
        this.addActionListener(e -> {
            switch (fileChooserType) {
                case File:
                    this.fileChooser = FileChooserBuilder.createFileChooser();
                case Directory:
                    this.fileChooser = FileChooserBuilder.createDirChooser();
            }
            this.fileChooser.setDialogTitle("保存");
            Window win = SwingUtilities.getWindowAncestor(this);
            int resultValue = fileChooser.showSaveDialog(win);
            if (resultValue == JFileChooser.APPROVE_OPTION) {
                this.saveConsumer.accept(fileChooser.getSelectedFile());
            }
            this.fileChooser.setSelectedFile(new File(""));

            // 使按钮失去焦点,给主窗口
            win.requestFocusInWindow();
        });
    }


    /**
     * 设置保存弹框
     *
     * @param title 保存弹框
     */
    public void setSaveTitle(String title) {
        fileChooser.setDialogTitle(title);
    }


    public JFileChooser getFileChooser() {
        return fileChooser;
    }

    public void setFileChooser(JFileChooser fileChooser) {
        this.fileChooser = fileChooser;
    }

    public Consumer<File> getSaveConsumer() {
        return saveConsumer;
    }

    public void setSaveConsumer(Consumer<File> saveConsumer) {
        this.saveConsumer = saveConsumer;
    }


    public void setFileChooserType(FileChooserType chooserType) {
        this.fileChooserType = chooserType;
    }


}
