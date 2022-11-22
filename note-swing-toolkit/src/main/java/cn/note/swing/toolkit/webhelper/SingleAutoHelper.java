package cn.note.swing.toolkit.webhelper;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import cn.note.service.toolkit.autostatic.HtmlTitleScanner;
import cn.note.service.toolkit.autostatic.SingleData;
import cn.note.swing.core.lifecycle.InitAction;
import cn.note.swing.core.util.FrameUtil;
import cn.note.swing.core.util.WinUtil;
import cn.note.swing.core.view.AbstractMigView;
import cn.note.swing.core.view.base.ButtonFactory;
import cn.note.swing.core.view.base.MessageBuilder;
import cn.note.swing.core.view.filechooser.FileChooserBuilder;
import cn.note.swing.core.view.icon.SvgIconFactory;
import cn.note.swing.core.view.item.ItemView;
import cn.note.swing.core.view.rsta.RstaBuilder;
import cn.note.swing.core.view.rsta.RstaLanguage;
import cn.note.swing.core.view.rsta.RstaTheme;
import cn.note.swing.core.view.theme.ThemeFlatLaf;
import cn.note.swing.toolkit.GroupConstants;
import net.miginfocom.swing.MigLayout;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.io.File;

/**
 * @description: 静态资源管理助手
 * @author: jee
 */
@ItemView(value = "静态助手", order = 102, category = GroupConstants.TOOLKIT, description = "生成静态目录助手", icon = SvgIconFactory.Menu.helper)
@Component
public class SingleAutoHelper extends AbstractMigView {
    /*路径描述*/
    private JLabel pathDesc;

    /*输入路径*/
    private JTextField path;

    /*json编辑器*/
    private RSyntaxTextArea rstaJsonEditor;

    /*代码编辑器*/
    private RTextScrollPane rstaJsonPane;

    /*构建按钮*/
    private JButton buildData;

    /*是否保存*/
    private JCheckBox isStore;

    /* sing-data 服务*/
    private SingleData singleData;

    /*保存json*/
    private JButton storeJson;

    /*浏览器打开*/
    private JButton browserOpen;

    /**
     * 定义布局
     */
    @Override
    protected MigLayout defineMigLayout() {
        return new MigLayout("insets 0,gapy 0", "[grow]", "[grow]");
    }

    @Override
    protected void init() {
        pathDesc = new JLabel("single路径:", JLabel.RIGHT);
//       F:\\Person_workspace\\note-single-html
        path = FileChooserBuilder.inputFileChooser("选择single-html");
        path.setText("D:\\Gitee_Workspaces\\note-single-html");

        buildData = ButtonFactory.primaryButton("构建");
        isStore = new JCheckBox("写入数据文件", true);
        storeJson = ButtonFactory.primaryButton("更新json至文件");
        browserOpen = new JButton("默认浏览器打开");

        rstaJsonPane = RstaBuilder.createRsPane(RstaLanguage.json);
        rstaJsonEditor = (RSyntaxTextArea) rstaJsonPane.getTextArea();
        // 开启代码折叠
        rstaJsonEditor.setCodeFoldingEnabled(true);
        RstaTheme.monokai.getTheme().apply(rstaJsonEditor);
        RstaBuilder.updateRstaFont(rstaJsonPane);
    }


    @Override
    protected void render() {
        super.setLayout(new MigLayout("wrap 1,align center"));
        JPanel row1 = new JPanel(new MigLayout("center"));
        row1.add(pathDesc, "w 10%");
        row1.add(path, "w 50%");
        super.add(row1, "w 80%,gaptop 10");

        JPanel row2 = new JPanel(new MigLayout("center"));
        row2.add(buildData, "gapright 5%");
        row2.add(isStore);
        super.add(row2, "w 80%,gaptop 10");

        super.add(rstaJsonPane, "w 80%,h 60%");

        JPanel row3 = new JPanel(new MigLayout("right"));
        row3.add(storeJson, "gapright 5");
        row3.add(browserOpen, "gapright 5");
        super.add(row3, "w 80%");


    }

    @InitAction
    private void actions() {


        buildData.addActionListener((e) -> {
            writeData();
        });

        storeJson.addActionListener((e) -> {
            writeJson2Data();
        });

        browserOpen.addActionListener((e) -> {
            if (singleData == null) {
                MessageBuilder.error(this, "请先构建验证!");
            } else {
                WinUtil.browserUrl(singleData.getIndexHtml());
            }

        });
    }


    /**
     * 写入数据
     */
    private void writeData() {
        String dirPath = path.getText().trim();
        File file = new File(dirPath);
        if (!file.exists()) {
            MessageBuilder.error(path, "路径不存在!");
            return;
        }
        if (!file.isDirectory()) {
            MessageBuilder.error(path, "路径不是目录!");
            return;
        }

        try {
            singleData = HtmlTitleScanner.buildData(dirPath);
            rstaJsonEditor.setText(singleData.getFormatData());
            if (isStore.isSelected()) {
                singleData.restore();
            }
        } catch (Exception e) {
            WinUtil.alertMulti("构建异常:\n" + e);
        }

    }


    /**
     * 写入数据
     */
    private void writeJson2Data() {
        if (singleData == null) {
            WinUtil.alert("请先构建验证!");
            return;
        }
        String json = rstaJsonEditor.getText().trim();
        JSONArray newDatas = null;
        try {
            newDatas = JSONUtil.parseArray(json);
        } catch (Exception e) {
            WinUtil.alertMulti("解析json异常:\n" + e);
            return;
        }
        try {
            singleData.updateData(newDatas);
            singleData.restore();
            WinUtil.layerMsg(FrameUtil.getFrame(), "已更新文件!");
        } catch (Exception e) {
            WinUtil.alertMulti("构建异常:\n" + e);
        }

    }

    public static void main(String[] args) {
        ThemeFlatLaf.install();
        FrameUtil.launchTime(SingleAutoHelper.class);
    }
}