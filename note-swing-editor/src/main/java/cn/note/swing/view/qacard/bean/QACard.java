package cn.note.swing.view.qacard.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * Q&A 格式内容
 */
@Setter
@Getter
public class QACard {

    public QACard() {

    }

    public QACard(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }


    /**
     * 问题
     */
    private String question;
    /**
     * 答案
     */
    private String answer;

}
