package cn.note.swing.view.note.category;

import cn.note.swing.core.view.AbstractMigView;
import cn.note.swing.core.view.base.ButtonFactory;
import cn.note.swing.core.view.icon.SvgIconFactory;
import cn.note.swing.core.view.theme.ThemeColor;
import cn.note.swing.core.view.tree.DragJXTree;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * @description: 分类工具类
 * 实现定位和折叠
 * @author: jee
 */
public class CategoryToolbarView extends AbstractMigView {

    /*分类视图*/
    private CategoryView categoryView;

    /*追踪当前视图*/
    @Getter
    private JButton traceView;

    /* 收起所有*/
    @Getter
    private JButton collapseAll;


    public CategoryToolbarView(CategoryView categoryView) {
        super(true);
        this.categoryView = categoryView;
        display();
    }


    @Override
    protected void init() {
        DragJXTree tree = categoryView.getTree();
        Color bgColor = view.getBackground();
        Color hoverColor = ThemeColor.dangerColor;
        Color iconColor = ThemeColor.themeColor;
        FlatSVGIcon traceViewIcon = SvgIconFactory.icon(SvgIconFactory.Category.trace);
        FlatSVGIcon collapseAllIcon = SvgIconFactory.icon(SvgIconFactory.Category.collapseAll);
        traceView = ButtonFactory.runIconButton(bgColor, hoverColor, traceViewIcon, iconColor);
        traceView.setToolTipText("定位当前");
        collapseAll = ButtonFactory.runIconButton(bgColor, hoverColor, collapseAllIcon, iconColor);
        collapseAll.setToolTipText("收起所有");
    }

    /**
     * 定义migLayout布局
     *
     * @return migLayout布局
     */
    @Override
    protected MigLayout defineMigLayout() {
        return new MigLayout("insets 0,gap 0", "[grow]", "[grow]");
    }

    /**
     * render视图
     */
    @Override
    protected void render() {
        JPanel rightOpt = new JPanel(new FlowLayout());
        rightOpt.add(collapseAll);
        rightOpt.add(traceView);
        JLabel resources = new JLabel("Resources");
        resources.setBackground(ThemeColor.themeColor);
        view.add(resources, "gapleft 5");
//        // 指定高度与arc一致
        view.add(rightOpt, "east");
    }
}
