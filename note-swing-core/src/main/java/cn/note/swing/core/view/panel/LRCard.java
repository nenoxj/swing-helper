package cn.note.swing.core.view.panel;

import javax.swing.*;

/**
 * 左右布局面板
 * 可以按照类名作为唯一key
 * 或自定义唯一key
 */
public class LRCard {


    public LRCard(String key, JComponent left) {
        this(key, left, null);
    }

    public LRCard(Class<?> clazz, JComponent left) {
        this(clazz.getName(), left, null);
    }

    public LRCard(Class<?> clazz, JComponent left, JComponent right) {
        this(clazz.getName(), left, right);
    }

    public LRCard(String key, JComponent left, JComponent right) {
        this.leftKey = key.concat("-left");
        this.rightKey = key.concat("-right");
        this.left = left;
        this.right = right;
    }


    private JComponent left;

    private JComponent right;

    private String leftKey;

    private String rightKey;

    public JComponent getLeft() {
        return left;
    }

    public void setLeft(JComponent left) {
        this.left = left;
    }

    public JComponent getRight() {
        return right;
    }

    public void setRight(JComponent right) {
        this.right = right;
    }

    public String getLeftKey() {
        return leftKey;
    }

    public String getRightKey() {
        return rightKey;
    }

}
