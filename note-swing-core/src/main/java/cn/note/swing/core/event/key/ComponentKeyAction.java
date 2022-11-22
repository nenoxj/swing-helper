package cn.note.swing.core.event.key;


import javax.swing.*;

/**
 * 组件按键事件
 *
 * @author jee
 */
public class ComponentKeyAction {
    /**
     * 动作名称
     */
    private String actionName;

    /**
     * 组件
     */
    private JComponent component;

    /**
     * 按键
     */
    private KeyStroke keyStroke;


    /**
     * 动作
     */
    private Action action;


    /**
     * 触发状态
     */
    private int actionStatus;


    private ComponentKeyAction(Builder builder) {
        this.actionName = builder.actionName;
        this.component = builder.component;
        this.keyStroke = builder.keyStroke;
        this.action = builder.action;
        this.actionStatus = builder.actionStatus;
        bindKeyAction();
    }


    /**
     * 组件绑定按键
     */
    public void bindKeyAction() {
        ActionMap contentActionMap = component.getActionMap();
        contentActionMap.put(actionName, action);
        InputMap contentInputMap = component.getInputMap(actionStatus);
        contentInputMap.put(keyStroke, actionName);
    }


    public static class Builder {


        /**
         * 动作名称
         */
        private String actionName;

        /**
         * 组件
         */
        private final JComponent component;

        /**
         * 按键
         */
        private KeyStroke keyStroke;


        /**
         * 动作
         */
        private Action action;


        /**
         * 触发状态
         */
        private int actionStatus;

        public Builder(JComponent component) {
            this.component = component;
        }


        public Builder actionName(String actionName) {
            this.actionName = actionName;
            return this;
        }

        public Builder keyStroke(KeyStroke keyStroke) {
            this.keyStroke = keyStroke;
            return this;
        }


        public Builder actionEvent(Action action) {
            this.action = action;
            return this;
        }

        public ComponentKeyAction build(KeyActionStatus status) {
            this.actionStatus = status.ordinal();
            return new ComponentKeyAction(this);
        }

    }


}
