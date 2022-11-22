package cn.note.swing.toolkit.carbon;

import cn.hutool.core.util.StrUtil;
import cn.note.swing.core.event.change.ChangeDocumentListener;
import cn.note.swing.core.lifecycle.InitAction;
import cn.note.swing.core.util.FrameUtil;
import cn.note.swing.core.util.SwingCoreUtil;
import cn.note.swing.core.view.AbstractMigView;
import cn.note.swing.core.view.base.ButtonFactory;
import cn.note.swing.core.view.base.MessageBuilder;
import cn.note.swing.core.view.filechooser.ButtonFileChooser;
import cn.note.swing.core.view.icon.SvgIconFactory;
import cn.note.swing.core.view.item.ItemView;
import cn.note.swing.core.view.rsta.RstaBuilder;
import cn.note.swing.core.view.rsta.RstaLanguage;
import cn.note.swing.core.view.rsta.RstaTheme;
import cn.note.swing.core.view.theme.ThemeColor;
import cn.note.swing.core.view.theme.ThemeFlatLaf;
import cn.note.swing.toolkit.GroupConstants;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import lombok.extern.slf4j.Slf4j;
import net.miginfocom.swing.MigLayout;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;


/**
 * 生成代码图片工具类
 */
@Slf4j
@ItemView(value = "代码图片", order = 103, category = GroupConstants.TOOLKIT, description = "生成代码图片")
@Component
public class CarbonImageView extends AbstractMigView {
    /**
     * 工具区域
     */
    private JPanel toolArea;
    /**
     * 主题
     */
    private JComboBox<String> themes;
    /**
     * 语言
     */
    private JComboBox<String> langs;
    /**
     * 导出
     */
    private JButton exportImage;
    /**
     * 保存
     */
    private ButtonFileChooser exportFile;
    /**
     * 背景
     */
    private JPanel bgArea;
    /**
     * 图标标题
     */
    private JLabel iconTitle;
    /**
     * 容器
     */
    private RTextScrollPane codePane;

    /**
     * 代码编辑器
     */
    private RSyntaxTextArea rstaEditor;
    /**
     * 保存文件选择器
     */
    private JFileChooser saveImageChooser;

    public CarbonImageView() {
        super(false, true);
    }

    @Override
    protected MigLayout defineMigLayout() {
        return new MigLayout("insets 20 0,gap 0", "[grow]", "[]30[]");
    }

    @Override
    protected void init() {
        // bg
        bgArea = new JPanel(new MigLayout("insets 30,flowy", "[grow,fill]", "[20,fill][grow,fill]"));
        Color bgColor = ThemeColor.themeColor;
        String style = StrUtil.format("arc:{};background:{};", 20, ThemeColor.toHEXColor(bgColor));
        bgArea.putClientProperty(FlatClientProperties.STYLE, style);

        // title
        FlatSVGIcon svgIcon = new FlatSVGIcon(SvgIconFactory.Tools.carbon_title);
        iconTitle = new JLabel(svgIcon);

        // editor 禁止滚动
        codePane = RstaBuilder.createRsPane();
        codePane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        codePane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        rstaEditor = (RSyntaxTextArea) codePane.getTextArea();
        rstaEditor.setLineWrap(true);

        // tool
        toolArea = new JPanel(new MigLayout(""));

        // theme
        String[] themeNames = Arrays.stream(RstaTheme.values()).map(Enum::name).toArray(String[]::new);
        themes = new JComboBox<>(themeNames);

        // langs
        String[] langNames = Arrays.stream(RstaLanguage.values()).map(Enum::name).toArray(String[]::new);
        langs = new JComboBox<>(langNames);

        exportFile = new ButtonFileChooser("保存图片", saveFile -> {
            // save image
//            FileNameExtensionFilter imageFilter = new FileNameExtensionFilter("png files (*.png)", "png");
//            exportFile.getFileChooser().setFileFilter(imageFilter);

            String fileName = saveFile.getName();
            if (!fileName.endsWith(".png")) {
                saveFile = Paths.get(saveFile.getParent(), fileName + ".png").toFile();
            }
            try {
                BufferedImage bi = SwingCoreUtil.exportComp2Image(bgArea);
                ImageIO.write(bi, "PNG", saveFile);
                MessageBuilder.ok(this, "保存成功!");
            } catch (IOException e) {
                log.error("保存图片失败!", e);
            }
        });

        ButtonFactory.decorativeButton(exportFile, ThemeColor.primaryColor);

        exportImage = ButtonFactory.primaryButton("复制图片");

    }

    @Override
    protected void render() {
        bgArea.add(iconTitle, "w 54!");
        bgArea.add(codePane, "grow");

        toolArea.add(new JLabel("语言:"));
        toolArea.add(langs);

        toolArea.add(new JLabel("主题:"));
        toolArea.add(themes);
        toolArea.add(exportFile, "gapleft 20");
        toolArea.add(exportImage, "gapleft 20");

        view.add(toolArea, "w 80%,center,cell 0 0");
        view.add(bgArea, "w 80%,center,cell 0 1");

        // 默认激活java语言
        langs.setSelectedItem("java");
        setEditorLanguage("java");
    }

    @InitAction("切换主题和code")
    private void toggleThemeAndCode() {
        themes.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                setEditorTheme(e.getItem().toString());
            }
        });

        langs.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                setEditorLanguage(e.getItem().toString());
            }
        });
    }

    /**
     * 设置编辑器主题
     *
     * @param theme 主题类型
     */
    private void setEditorTheme(String theme) {
        RstaTheme.valueOf(theme).getTheme().apply(rstaEditor);
        RstaBuilder.updateRstaFont(codePane);
    }

    /**
     * 设置编辑器语言
     *
     * @param lang 语言类型
     */
    private void setEditorLanguage(String lang) {
        RstaLanguage.valueOf(lang).apply(rstaEditor);
    }

    @InitAction("导出至粘贴板")
    private void exportClip() {
        exportImage.addActionListener((e) -> {
            exportImage.requestFocus();
            SwingCoreUtil.exportComp2ClipImage(bgArea, () -> {
                MessageBuilder.ok(this, "已复制图片至粘贴板!");
            });
        });
    }

    @InitAction("控制代码编辑的滚动被父窗口接管")
    private void hideEditorScroll() {
        codePane.getTextArea().getDocument().addDocumentListener(new ChangeDocumentListener() {
            @Override
            public void update(DocumentEvent e) {
                view.revalidate();
                view.repaint();
            }
        });
    }


    public static void main(String[] args) {
        ThemeFlatLaf.install();
        FrameUtil.launchTime(CarbonImageView.class);
    }
}
