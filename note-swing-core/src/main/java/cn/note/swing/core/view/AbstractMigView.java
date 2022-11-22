package cn.note.swing.core.view;

import cn.hutool.core.util.StrUtil;
import cn.note.swing.core.lifecycle.InitListener;
import cn.note.swing.core.util.WinUtil;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.swing.*;
import java.awt.*;

/**
 * @description: miglayout布局的panel
 * 内置快速操作 view
 * @author: jee
 */
public abstract class AbstractMigView extends JPanel implements InitListener {

    private boolean finish;
    /**
     * 主视图
     */
    protected JPanel view;


    /**
     * 加载错误
     */
    private boolean loadError;

    /**
     * clone 可以使用AbstractMigView 的静态布局变量
     * 否则会有样式覆盖现象
     */
    public AbstractMigView() {
        this(false);
    }

    /**
     * 是否延迟渲染视图
     *
     * @param lazy 是否延迟
     */
    public AbstractMigView(boolean lazy) {
        this(lazy, false);
    }

    /**
     * @param lazy   是否延迟显示
     * @param scroll 是否滚动
     */
    public AbstractMigView(boolean lazy, boolean scroll) {
        super(true);
        super.setLayout(new GridLayout(1, 1));
        view = new JPanel(defineMigLayout());
        if (!lazy) {
            display(scroll);
        }
    }

    /**
     * 定义migLayout布局
     *
     * @return migLayout布局
     */
    protected abstract MigLayout defineMigLayout();

    /**
     * 初始化成员对象
     */
    protected void init() {

    }


    /**
     * render视图
     */
    protected abstract void render();

    /**
     * 自测代码
     */
    protected void test() {
    }

    /**
     * 默认创建不滚动的视图
     */
    protected void display() {
        display(false);
    }

    /**
     * 渲染视图
     *
     * @param scroll 是否可滚动的视图
     */
    protected void display(boolean scroll) {
        init();
        if (!loadError) {
            test();
            render();
            if (scroll) {
                JScrollPane scrollPane = new JScrollPane(view);
                scrollPane.setBorder(BorderFactory.createEmptyBorder());
                super.add(scrollPane);
            } else {
                super.add(view);
            }
            bindEvents();
            this.finish = true;
        }
    }

    /**
     * 拒绝加载
     */
    protected void refuse() {
        this.refuse(null, null);
    }

    /**
     * @param error 错误内容
     */
    protected void refuse(String error) {
        this.refuse(error, null);
    }

    /**
     * 拒绝加载
     *
     * @param error 错误内容
     * @param e     异常原因
     */
    protected void refuse(String error, Exception e) {
        this.loadError = true;
        showError(error, e);
    }

    private void showError(String error, Exception e) {
        if (StrUtil.isBlank(error)) {
            return;
        }
        if (e == null) {
            WinUtil.alert(error);
        } else {
            WinUtil.alert("{} \n {}", error, ExceptionUtils.getStackTrace(e));

        }
    }

    public boolean isFinish() {
        return finish;
    }
}
