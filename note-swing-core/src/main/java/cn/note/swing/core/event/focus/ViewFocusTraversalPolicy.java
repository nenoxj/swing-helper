package cn.note.swing.core.event.focus;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * 视图焦点管理器
 * 指定视图移动的焦点 配合Tab键使用
 *
 * @author jee
 */
public class ViewFocusTraversalPolicy extends FocusTraversalPolicy {

    private List<Component> focusOrderList;


    public ViewFocusTraversalPolicy(List<Component> focusOrderList) {
        this.focusOrderList = focusOrderList;
    }

    /**
     * @param focusCycleRoot Container
     * @param aComponent     Component
     * @return Component
     */
    public Component getComponentAfter(Container focusCycleRoot,
                                       Component aComponent) {

        int nIDx = focusOrderList.indexOf(aComponent);

        int nAfter;
        if (nIDx == focusOrderList.size() - 1) {
            nAfter = 0;
        } else {
            nAfter = nIDx + 1;
        }

        Component component = focusOrderList.get(nAfter);

        boolean bContinue = false;

        if (!component.isEnabled()) {
            bContinue = true;
        } else if (component instanceof JTextField) {
            JTextField txtField = (JTextField) component;
            if (!txtField.isEditable()) {
                bContinue = true;
            }
        }

        if (bContinue) {
            return getComponentAfter(focusCycleRoot, component);
        } else {
            return component;
        }
    }

    /**
     * @param focusCycleRoot Container
     * @param aComponent     Component
     * @return Component
     */
    public Component getComponentBefore(Container focusCycleRoot,
                                        Component aComponent) {

        int nIDx = focusOrderList.indexOf(aComponent);

        int nBefore;
        if (nIDx == 0) {
            nBefore = focusOrderList.size() - 1;
        } else {
            nBefore = nIDx - 1;
        }

        Component component = focusOrderList.get(nBefore);

        boolean bContinue = false;

        if (!component.isEnabled()) {
            bContinue = true;
        } else if (component instanceof JTextField) {
            JTextField txtField = (JTextField) component;
            if (!txtField.isEditable()) {
                bContinue = true;
            }
        }

        if (bContinue) {
            return getComponentBefore(focusCycleRoot, component);
        } else {
            return component;
        }

    }

    /**
     * @param focusCycleRoot Container
     * @return Component
     */
    public Component getDefaultComponent(Container focusCycleRoot) {
        Component component = focusOrderList.get(0);
        return component;
    }

    /**
     * @param focusCycleRoot Container
     * @return Component
     */
    public Component getLastComponent(Container focusCycleRoot) {
        Component component = focusOrderList.get(focusOrderList.size() - 1);
        return component;
    }

    /**
     * @param focusCycleRoot Container
     * @return Component
     */
    public Component getFirstComponent(Container focusCycleRoot) {
        Component component = focusOrderList.get(0);
        return component;
    }
}