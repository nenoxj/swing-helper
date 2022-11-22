package cn.note.swing.core.event.key;

import javax.swing.*;

/**
 * @see JComponent   WHEN_FOCUSED  WHEN_ANCESTOR_OF_FOCUSED_COMPONENT WHEN_IN_FOCUSED_WINDOW
 * 从JComponent 取出key发生的三种状态作为枚举使用
 * <p>
 * Constant used for <code>registerKeyboardAction</code> that
 * means that the command should be invoked when
 * the component has the focus.
 * public static final int WHEN_FOCUSED = 0;
 * <p>
 * Constant used for <code>registerKeyboardAction</code> that
 * means that the command should be invoked when the receiving
 * component is an ancestor of the focused component or is
 * itself the focused component.
 * public static final int WHEN_ANCESTOR_OF_FOCUSED_COMPONENT = 1;
 * <p>
 * /**
 * Constant used for <code>registerKeyboardAction</code> that
 * means that the command should be invoked when
 * the receiving component is in the window that has the focus
 * or is itself the focused component.
 * public static final int WHEN_IN_FOCUSED_WINDOW = 2;
 *
 *
 * 焦点的三种状态
 * @author jee
 */

public enum KeyActionStatus {
    WHEN_FOCUSED, WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, WHEN_IN_FOCUSED_WINDOW

}
