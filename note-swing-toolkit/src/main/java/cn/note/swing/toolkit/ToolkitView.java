package cn.note.swing.toolkit;

import cn.note.swing.core.util.FrameUtil;
import cn.note.swing.core.view.AbstractMigCard;
import cn.note.swing.core.view.item.ItemNode;
import cn.note.swing.core.view.item.ItemScanner;
import cn.note.swing.core.view.item.MenuItemView;
import cn.note.swing.core.view.panel.LRCard;
import cn.note.swing.core.view.theme.ThemeFlatLaf;

import java.util.List;

/**
 * @author jee
 * @version 1.0
 */
public class ToolkitView extends AbstractMigCard {

    private MenuItemView menuItemView;

    public ToolkitView() {

    }

    public ToolkitView(boolean card) {
        super(card);
    }

    @Override
    public LRCard getCardView() {
        return new LRCard(this.getClass(), menuItemView.getItemSelector().getSelectorComponent(), menuItemView.getItemContainer());
    }

    @Override
    protected void init() {
        List<ItemNode> menuItems = ItemScanner.scanAllItemView("cn.note.swing.toolkit");
        menuItemView = new MenuItemView(menuItems);
    }

    /**
     * render视图
     */
    @Override
    protected void render() {
        view.add(menuItemView.create(), "grow");
        menuItemView.setDefaultSelectedItem();
    }

    public static void main(String[] args) {
        ThemeFlatLaf.install();
        FrameUtil.launchTime(ToolkitView.class);
    }

}
