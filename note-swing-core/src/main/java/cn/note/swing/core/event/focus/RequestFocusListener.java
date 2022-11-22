package cn.note.swing.core.event.focus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 组件请求获取焦点
 * <p>
 * public class RequestFocusListener implements AncestorListener
 * {
 * public void ancestorAdded(final AncestorEvent e)
 * {
 * final AncestorListener al= this;
 * SwingUtilities.invokeLater(new Runnable(){
 *
 * @Override public void run()
 * {
 * JComponent component = (JComponent)e.getComponent();
 * component.requestFocusInWindow();
 * component.removeAncestorListener( al );
 * }
 * });
 * }
 * <p>
 * public void ancestorMoved(AncestorEvent e) {}
 * public void ancestorRemoved(AncestorEvent e) {}
 *
 * @author jee
 * }
 */
public class RequestFocusListener implements HierarchyListener {
    public RequestFocusListener() {
    }

    @Override
    public void hierarchyChanged(HierarchyEvent e) {
        final Component c = e.getComponent();
        if (c.isShowing() && (e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0) {
            final Window toplevel = SwingUtilities.getWindowAncestor(c);
            toplevel.addWindowFocusListener(new WindowAdapter() {
                @Override
                public void windowGainedFocus(WindowEvent e) {
                    SwingUtilities.invokeLater(() -> {
                        c.requestFocusInWindow();
                        toplevel.removeWindowFocusListener(this);
                    });
                }
            });
            c.removeHierarchyListener(this);
        }
    }
}
