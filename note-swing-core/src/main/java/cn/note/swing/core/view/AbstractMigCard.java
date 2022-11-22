package cn.note.swing.core.view;

import cn.note.swing.core.view.panel.LRCardView;
import net.miginfocom.swing.MigLayout;

/**
 * 用作card 视图
 * 默认实现全屏布局
 * <p>
 * leftView 和rightView 只可以用作测试
 */
public abstract class AbstractMigCard extends AbstractMigView implements LRCardView {


    public AbstractMigCard() {
        this(false);
    }

    public AbstractMigCard(boolean card) {
        super(true);
        if (!card) {
            super.display();
        } else {
            this.init();
            this.bindEvents();
        }
    }

    @Override
    protected MigLayout defineMigLayout() {
        return new MigLayout("insets 0,gap 0", "[grow]", "[grow]");
    }

}
