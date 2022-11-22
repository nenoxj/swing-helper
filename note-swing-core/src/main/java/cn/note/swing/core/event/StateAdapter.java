package cn.note.swing.core.event;

import lombok.extern.slf4j.Slf4j;

import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;

/**
 * 状态监听器
 * 指定组件显示时  某些获取焦点
 * ComponentListener 监听组件显示 隐藏
 * AncestorListener 监听组件添加 删除
 * HierarchyListener 监听所有
 *
 * @author jee
 */
@Slf4j
public class StateAdapter implements ComponentListener, AncestorListener, HierarchyListener {
    @Override
    public void componentResized(ComponentEvent e) {

    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }

    @Override
    public void ancestorAdded(AncestorEvent event) {

    }

    @Override
    public void ancestorRemoved(AncestorEvent event) {

    }

    @Override
    public void ancestorMoved(AncestorEvent event) {

    }

    @Override
    public void hierarchyChanged(HierarchyEvent e) {
//        System.out.println("Components Change: " + e.getChanged());
//        if ((e.getChangeFlags() & HierarchyEvent.DISPLAYABILITY_CHANGED) != 0) {
//            if (e.getComponent().isDisplayable()) {
//                System.out.println("Components DISPLAYABILITY_CHANGED : "
//                        + e.getChanged());
//            } else {
//                System.out.println("Components DISPLAYABILITY_CHANGED : "
//                        + e.getChanged());
//            }
//        }
//        if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0) {
//            if (e.getComponent().isDisplayable()) {
//                System.out.println("Components SHOWING_CHANGED : " + e.getChanged());
//            } else {
//                System.out.println("Components SHOWING_CHANGED : " + e.getChanged());
//            }
//        }
    }
}
