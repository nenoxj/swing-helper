package cn.note.service.toolkit.git;

import org.eclipse.jgit.errors.UnsupportedCredentialItem;
import org.eclipse.jgit.transport.*;
import org.eclipse.jgit.transport.CredentialItem.CharArrayType;
import org.eclipse.jgit.transport.CredentialItem.InformationalMessage;
import org.eclipse.jgit.transport.CredentialItem.StringType;
import org.eclipse.jgit.transport.CredentialItem.YesNoType;

import javax.swing.*;
import java.awt.*;


/**
 * 自定义git验证器
 * <p>
 * 修改AwtAuthenticator ,支持保存账号密码
 * <p>
 * org.eclipse.jgit.awtui.AwtAuthenticator
 * <dependency>
 * <groupId>org.eclipse.jgit</groupId>
 * <artifactId>org.eclipse.jgit.ui</artifactId>
 * <version>5.13.1.202206130422-r</version>
 * </dependency>
 */
public class CustomGitAuthenticator extends CredentialsProvider {

    public static String DIALOG_TITLE = "git权限验证";
    public static String USERNAME = "用户名：";
    public static String PASSWORD = "   密码：";

    public static void install() {
        CustomGitAuthenticator c = new CustomGitAuthenticator();
        CredentialsProvider cp = new ChainingCredentialsProvider(new NetRCCredentialsProvider(), c);
        CredentialsProvider.setDefault(cp);
    }

    public boolean isInteractive() {
        return true;
    }

    public boolean supports(CredentialItem... items) {
        CredentialItem[] var5 = items;
        int var4 = items.length;

        for (int var3 = 0; var3 < var4; ++var3) {
            CredentialItem i = var5[var3];
            if (!(i instanceof StringType) && !(i instanceof CharArrayType) && !(i instanceof YesNoType) && !(i instanceof InformationalMessage)) {
                return false;
            }
        }

        return true;
    }

    public boolean get(URIish uri, CredentialItem... items) throws UnsupportedCredentialItem {
        switch (items.length) {
            case 0:
                return true;
            case 1:
                CredentialItem item = items[0];
                if (item instanceof InformationalMessage) {
                    JOptionPane.showMessageDialog((Component) null, item.getPromptText(), "warning", 1);
                    return true;
                } else {
                    if (item instanceof YesNoType) {
                        YesNoType v = (YesNoType) item;
                        int r = JOptionPane.showConfirmDialog((Component) null, v.getPromptText(), "warning", 0);
                        switch (r) {
                            case -1:
                            case 2:
                            default:
                                return false;
                            case 0:
                                v.setValue(true);
                                return true;
                            case 1:
                                v.setValue(false);
                                return true;
                        }
                    }

                    return interactive(uri, items);
                }
            default:
                return interactive(uri, items);
        }
    }


    private static JLabel createLabel(CredentialItem item) {
        String text = item.getPromptText();
        switch (text) {
            case "Username":
                return new JLabel(USERNAME);
            case "Password":
                return new JLabel(PASSWORD);
        }
        return new JLabel(text);

    }


    private static boolean interactive(URIish uri, CredentialItem[] items) {
        GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0D, 1.0D, 18, 0, new Insets(0, 0, 0, 0), 0, 0);
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        JTextField[] texts = new JTextField[items.length];

        int i;
        CredentialItem item;
        for (i = 0; i < items.length; ++i) {
            item = items[i];
            if (!(item instanceof StringType) && !(item instanceof CharArrayType)) {
                if (!(item instanceof InformationalMessage)) {
                    throw new UnsupportedCredentialItem(uri, item.getPromptText());
                }

                gbc.fill = 0;
                gbc.gridwidth = 0;
                gbc.gridx = 0;
//                panel.add(new JLabel(item.getPromptText()), gbc);
                panel.add(createLabel(item), gbc);
                ++gbc.gridy;
            } else {
                gbc.fill = 0;
                gbc.gridwidth = -1;
                gbc.gridx = 0;
//                panel.add(new JLabel(item.getPromptText()), gbc);
                panel.add(createLabel(item), gbc);
                gbc.fill = 2;
                gbc.gridwidth = -1;
                gbc.gridx = 1;
                if (item.isValueSecure()) {
                    texts[i] = new JPasswordField(20);
                } else {
                    texts[i] = new JTextField(20);
                }

                panel.add(texts[i], gbc);
                ++gbc.gridy;
            }
        }

//        if (JOptionPane.showConfirmDialog((Component) null, panel, UIText.get().authenticationRequired, 2, 3) != 0) {
        if (JOptionPane.showConfirmDialog((Component) null, panel, DIALOG_TITLE, 2, 3) != 0) {
            return false;
        } else {
            for (i = 0; i < items.length; ++i) {
                item = items[i];
                JTextField f = texts[i];
                if (item instanceof StringType) {
                    StringType v = (StringType) item;
                    if (f instanceof JPasswordField) {
                        v.setValue(new String(((JPasswordField) f).getPassword()));
                    } else {
                        v.setValue(f.getText());
                    }
                } else if (item instanceof CharArrayType) {
                    CharArrayType v = (CharArrayType) item;
                    if (f instanceof JPasswordField) {
                        v.setValueNoCopy(((JPasswordField) f).getPassword());
                    } else {
                        v.setValueNoCopy(f.getText().toCharArray());
                    }
                }
            }

            /*
             * ******************************
             * 验证通过,自定义GIT存储至注册表
             * *********************************
             */
            String gitUrl = uri.toString();
            String username = texts[0].getText();
            String password = new String(((JPasswordField) texts[1]).getPassword());
            GitCredit gitCredit = new GitCredit(gitUrl, username, password);
            GitUtil.storeCredit(gitCredit);

            return true;
        }
    }
}
