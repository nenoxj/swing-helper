package cn.note.swing.core.view.item;

import cn.note.swing.core.lifecycle.State;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * @author jee
 * 菜单选项,构建的基础组件维护类
 */
@Slf4j
public class ItemNode {

    /**
     * property 名称
     */
    public static final String ITEM_PROPERTY = "itemComponent";

    /**
     * 状态标识
     */
    private State state;

    /**
     * 动态组件构建
     */
    private DynamicComponent<Component> dynamicComponent;

    /**
     * 节点名称
     */
    @Setter
    @Getter
    private String name;

    /**
     * 节点分类
     */
    @Setter
    @Getter
    private String category;

    /**
     * 加载类信息
     */
    @Setter
    @Getter
    private Class<?> itemClass;

    /**
     * 排序
     */
    @Getter
    @Setter
    private int order;

    /**
     * 描述
     */
    @Getter
    @Setter
    private String description;


    /**
     * 图标
     */
    @Getter
    @Setter
    private String icon;


    private PropertyChangeSupport pcs;

    private Component component;

    public ItemNode(Class<?> itemClass, DynamicComponent<Component> dynamicComponent) {

        this.itemClass = itemClass;
        this.dynamicComponent = dynamicComponent;
        ItemView itemView = itemClass.getAnnotation(ItemView.class);
        /* 获取注解信息*/
        this.name = itemView.value();
        this.category = itemView.category();
        this.order = itemView.order();
        this.icon = itemView.icon();
        this.description = itemView.description();

        this.state = State.UNINITIALIZED;
        this.pcs = new PropertyChangeSupport(this);
    }

    /**
     * 添加change监听
     *
     * @param pcl 属性监听
     */
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        pcs.addPropertyChangeListener(pcl);
    }

    /**
     * 移除change监听
     *
     * @param pcl 属性监听
     */
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        pcs.removePropertyChangeListener(pcl);
    }

    /**
     * 设置state状态
     *
     * @param state 状态
     */
    protected void setState(State state) {
        State oldState = this.state;
        this.state = state;
//        Console.log("item: {} stateStatus:{}", getName(), state);
        pcs.firePropertyChange("state", oldState, state);
    }


    /**
     * 创建节点组件
     * <p>
     * SpringContext 获取上下文注册组件对象
     *
     * @return swing组件
     * @throws Exception 无法创建组件
     */
    public Component createNodeComponent() throws Exception {
        Component component = null;
        try {
            component = dynamicComponent.instance(this.itemClass);
            this.setNodeComponent(component);
        } catch (Exception e) {
            log.error(getName() + "初始化异常:", e);
            setState(State.FAILED);
            throw e;
        }
        return component;
    }

    public void setNodeComponent(Component component) {
        if (component != null && !itemClass.isInstance(component)) {
            setState(State.FAILED);
            throw new IllegalArgumentException("Component未实例化不能使用: " +
                    itemClass.getCanonicalName());
        }
        Component old = this.component;
        this.component = component;
        if (component != null) {
            setState(State.INITIALIZED);
        } else {
            setState(State.UNINITIALIZED);
        }

        pcs.firePropertyChange(ITEM_PROPERTY, old, component);
    }


    public Component getNodeComponent() {
        return this.component;
    }

    /**
     * 开始实例化
     */
    public void startInitializing() {
        setState(State.INITIALIZING);
    }

    /**
     * 打开item
     */
    public void start() {
        setState(State.RUNNING);
    }

    /**
     * 关闭item
     */
    public void stop() {
        setState(State.STOPPED);
    }


    @Override
    public String toString() {
        return "ItemNode{" +
                "name='" + name + '\'' +
                ", itemClass=" + itemClass.getName() +
                '}';
    }

}
