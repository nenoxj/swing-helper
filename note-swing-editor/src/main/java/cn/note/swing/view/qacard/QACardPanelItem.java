package cn.note.swing.view.qacard;

import cn.hutool.core.swing.clipboard.ClipboardUtil;
import cn.hutool.core.util.StrUtil;
import cn.note.swing.core.event.ConsumerAction;
import cn.note.swing.core.event.key.KeyActionFactory;
import cn.note.swing.core.util.SwingCoreUtil;
import cn.note.swing.core.view.base.MessageBuilder;
import cn.note.swing.core.view.theme.DarkThemeUI;
import cn.note.swing.view.qacard.bean.QACard;
import org.jdesktop.swingx.JXTitledPanel;

import javax.swing.*;
import java.awt.*;

/**
 * QACardPanelList 的子模块
 */
public class QACardPanelItem extends JXTitledPanel implements DarkThemeUI {

    private QACardPanelList qaCardPanelList;
    private QACard qaCard;
    private JTextField question;
    private JTextArea answer;
    private JButton optDelete;
    private JButton optCopy;
    private JButton optConfirm;
    private JButton optCancel;
    private JButton optEdit;


    private final int columnSize = 15;

    public QACardPanelItem(QACardPanelList qaCardPanelList, QACard qaCard) {
        this.qaCardPanelList = qaCardPanelList;
        this.qaCard = qaCard;
        answer = new JTextArea(qaCard.getAnswer());
        answer.setEditable(false);
        answer.setLineWrap(true);
        JScrollPane textScroll = new JScrollPane(answer);
        textScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        super.setContentContainer(textScroll);
        super.setTitlePainter(backgroundPainter());
        createQuestion();
        createOptPanel();
    }

    public QACard getQACard() {
        return qaCard;
    }

    /**
     * 创建问题
     */
    private void createQuestion() {
        question = lineInnerTextField(qaCard.getQuestion(), 1);
        KeyActionFactory.bindEscAction(question, new ConsumerAction(e -> setConfirmStatus(false)));
        // 设置默认大小 无法实现动态更新大小
        question.setPreferredSize(new Dimension(150, 30));
        super.setLeftDecoration(question);
    }


    protected void deleteAction() {
        String questionText = question.getText();
        if (StrUtil.isBlank(questionText) || StrUtil.isBlank(qaCard.getQuestion())) {
            qaCardPanelList.deleteQACardItem(QACardPanelItem.this, false);
        } else {
            deleteDialogWindow(this, "删除问题", questionText, () -> {
                qaCardPanelList.deleteQACardItem(QACardPanelItem.this, true);
            });
        }
    }

    /**
     * 创建操作面板
     */
    private void createOptPanel() {

        JPanel optPanel = new JPanel();
        optPanel.setBackground(backgroundColor());
        optCopy = optIconCopy(e -> ClipboardUtil.setStr(answer.getText()));
        optDelete = optIconDelete(e -> deleteAction());

        optConfirm = optIconConfirm(e -> setConfirmStatus(true));

        optCancel = optIconCancel(e -> setConfirmStatus(false));

        optEdit = optIconEdit(e -> setEditStatus(true));

        optPanel.add(optDelete);
        optPanel.add(optEdit);
        optPanel.add(optCopy);
        optPanel.add(optConfirm);
        optPanel.add(optCancel);
        super.setRightDecoration(optPanel);
    }


    /**
     * 设置确认状态
     */
    private void setConfirmStatus(boolean confirmStatus) {
        String questionText = question.getText();
        if (confirmStatus && StrUtil.isBlank(questionText)) {
            MessageBuilder.error(this, "问题不能为空!");
            question.requestFocusInWindow();
            return;
        }

        if (confirmStatus) {
            String answerText = answer.getText();
            if (!StrUtil.equals(questionText, qaCard.getQuestion()) || !StrUtil.equals(answerText, qaCard.getAnswer())) {
                qaCard.setQuestion(questionText);
                qaCard.setAnswer(answerText);
                qaCardPanelList.save();
            }
            question.setCaretPosition(0);
        } else {
            answer.setText(qaCard.getAnswer());
            question.setText(qaCard.getQuestion());
        }
        setEditStatus(false);
    }

    /**
     * 设置新增状态
     */
    public void setAddStatus() {
        setEditStatus(true);
        optCancel.setVisible(false);
        SwingCoreUtil.onceTimer(30, () -> question.requestFocusInWindow());
    }

    /**
     * 设置编辑状态
     */
    private void setEditStatus(boolean editStatus) {
        question.setEnabled(editStatus);
        answer.setEditable(editStatus);
        optCopy.setVisible(!editStatus);
        optEdit.setVisible(!editStatus);
        optConfirm.setVisible(editStatus);
        optCancel.setVisible(editStatus);
        answer.requestFocusInWindow();
    }
}