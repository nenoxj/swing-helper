package cn.note.swing.core.view.theme;

import javax.swing.*;

/**
 * 系统图标
 */
public final class ThemeIcon {
    /**
     * 树展开图标
     */
    public static Icon expandedIcon = UIManager.getIcon("Tree.expandedIcon");
    /**
     * 树收起图标
     */
    public static Icon collapsedIcon = UIManager.getIcon("Tree.collapsedIcon");
    /**
     * 关闭图标
     */
    public static Icon closeIcon = UIManager.getIcon("InternalFrame.closeIcon");
    /**
     * tree的默认子节点图标,与文件图标类似
     */
    public static Icon treeLeafIcon = UIManager.getIcon("Tree.leafIcon");
    /**
     * tree的默认打开图标 与文件夹打开类似
     */
    public static Icon treeOpenIcon = UIManager.getIcon("Tree.openIcon");
    /**
     * tree的默认关闭图标 与文件夹类似
     */
    public static Icon treeCloseIcon = UIManager.getIcon("Tree.closedIcon");
}
