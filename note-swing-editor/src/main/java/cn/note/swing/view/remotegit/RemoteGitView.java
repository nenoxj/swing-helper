package cn.note.swing.view.remotegit;

import cn.hutool.json.JSONObject;
import cn.note.service.toolkit.git.GitConsumer;
import cn.note.service.toolkit.git.GitTemplate;
import cn.note.swing.core.event.ConsumerAction;
import cn.note.swing.core.lifecycle.InitAction;
import cn.note.swing.core.util.FrameUtil;
import cn.note.swing.core.util.WinUtil;
import cn.note.swing.core.view.AbstractMigView;
import cn.note.swing.core.view.base.ButtonFactory;
import cn.note.swing.core.view.base.MessageBuilder;
import cn.note.swing.core.view.wrapper.StyleWrapper;
import cn.note.swing.core.view.icon.SvgIconFactory;
import cn.note.swing.core.view.loading.LoadingUtil;
import cn.note.swing.core.view.panel.CollapsePanel;
import cn.note.swing.core.view.theme.ThemeColor;
import cn.note.swing.core.view.theme.ThemeFlatLaf;
import cn.note.swing.store.NoteContext;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import net.miginfocom.swing.MigLayout;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.jdesktop.swingx.JXLabel;

import javax.swing.*;
import java.awt.*;

/**
 * 实现git 同步
 */
@Slf4j
public class RemoteGitView extends AbstractMigView {

    /**
     * git设置
     */
    private JButton gitSetting;

    /**
     * 折叠面板
     */
    private CollapsePanel collapsePanel;

    /* git操作面板*/
    private JPanel gitOperationPanel;

    /*git操作摸板*/
    private GitTemplate gitTemplate;

    private JXLabel gitStatus;

    private boolean executeError;


    @Override
    protected MigLayout defineMigLayout() {
        return new MigLayout("gap 0,insets 0", "[grow]", "[grow]");
    }

    @Override
    protected void init() {
        collapsePanel = new CollapsePanel();
        FlatSVGIcon svgIcon = SvgIconFactory.icon(SvgIconFactory.Common.setting);
        gitSetting = ButtonFactory.runIconButton(ThemeColor.panelColor, ThemeColor.dangerColor, svgIcon, collapsePanel.getTitleForeground());
        // 操作面板
        gitOperationPanel = new JPanel(new MigLayout("wrap 1,gap 0 2,insets 0", "[grow]"));


        createGitConsumer("commit", (git) -> {
            if (git.notCleanStatus()) {
                git.autoCommit();
            } else {
                executeError = true;
                gitStatus.setText("Not Changed!");
            }
        });

        createGitConsumer("pull", (git) -> {
            if (git.notCleanStatus()) {
                MessageBuilder.warn(this, "请先清理状态文件再推送远程");
                executeError = true;
            } else {
                git.pull();
                NoteContext.getInstance().getCategoryView().reloadRoot();
            }
        });
        createGitConsumer("push", (git) -> {
            if (gitTemplate.notCleanStatus()) {
                MessageBuilder.warn(this, "请先清理状态文件再更新本地");
                executeError = true;
            } else {
                git.push();
                NoteContext.getInstance().getCategoryView().reloadRoot();
            }
        });

        createGitAction("status", new ConsumerAction(e -> {
            try {
                JSONObject status = gitTemplate.status();
                WinUtil.showDialogForHelper("git status", new JScrollPane(new JTextArea(status.toStringPretty())));
            } catch (GitAPIException e1) {
                MessageBuilder.error(gitOperationPanel, "git操作异常:" + e1.getMessage());
            }
        }));

        gitStatus = new JXLabel();
        gitStatus.setMaxLineSpan(200);
        gitOperationPanel.add(gitStatus, "h 25::");
    }

    @Override
    protected void render() {
        collapsePanel.setBorder(BorderFactory.createEmptyBorder());
        collapsePanel.setTitle("Sync Git");
        collapsePanel.setRightDecoration(gitSetting);
        collapsePanel.setContentContainer(gitOperationPanel);
        view.add(collapsePanel, "grow");
    }


    private void createGitAction(String text, ConsumerAction consumerAction) {
        createGitOptButton(text, consumerAction, null);
    }

    private void createGitConsumer(String text, GitConsumer gitConsumer) {
        createGitOptButton(text, null, gitConsumer);
    }

    /**
     * 创建git操作按钮
     */
    private void createGitOptButton(String text, ConsumerAction consumerAction, GitConsumer gitConsumer) {
        JButton button = new JButton(text);
        button.setEnabled(false);
        StyleWrapper.create().arc(0).borderWidth(0).hoverBackground(ThemeColor.primaryColor).build(button);
        gitOperationPanel.add(button, "grow");

        if (consumerAction != null) {
            button.addActionListener(consumerAction);
        }
        if (gitConsumer != null) {
            button.addActionListener(e -> {
                if (gitTemplate == null) {
                    MessageBuilder.error(button, "git操作对象丢失!");
                    return;
                }
                String loadingTitle = "git " + text;
                executeError = false;
                gitStatus.setText("");
                LoadingUtil.progressLoading(loadingTitle, () -> {
                            try {
                                gitConsumer.accept(gitTemplate);
                                if (!executeError) {
                                    gitStatus.setText(text + " success");
                                }
                            } catch (Exception e1) {
                                MessageBuilder.error(button, "git操作异常:" + e1.getMessage());
                                gitStatus.setText(text + " error:" + e1.getMessage());
                            } finally {
                                gitTemplate.close();
                            }
                        }
                );
            });

        }
    }

    @InitAction("git设置")
    private void gitConfigSetting() {
        gitSetting.addActionListener(e -> WinUtil.showDialogForCustom(collapsePanel, "设置git", new RemoteSettingView()));
    }


    @Subscribe
    public void subscribeGitTemplate(GitTemplate gitTemplate) {
        if (gitTemplate != null) {
            this.gitTemplate = gitTemplate;
            gitStatus.setText("gitTemplate success");
            for (Component c : gitOperationPanel.getComponents()) {
                c.setEnabled(true);
            }
        }
    }

    public static void main(String[] args) {
        ThemeFlatLaf.install();
        FrameUtil.launchTime(RemoteGitView.class);
    }
}
