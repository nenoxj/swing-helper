package cn.note.swing.core.view.item;

import java.awt.*;

/**
 * 动态组件
 */
@FunctionalInterface
public interface DynamicComponent<T extends Component> {
    /**
     * 动态实例化swing组件
     *
     * @param clazz 动态类
     * @return 返回swing组件
     * @throws Exception 实例化异常
     */
    T instance(Class<?> clazz) throws Exception;
}
