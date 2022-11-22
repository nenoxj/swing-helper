package cn.note.swing.core.view.item;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * Item扫描器
 */
public class ItemScanner {


    /**
     * 默认使用java反射实例化对象
     *
     * @param packageNames 包名,如cn.note.swing
     * @return ItemNode的集合对象
     */
    public static List<ItemNode> scanAllItemView(String packageNames) {
        return scanAllItemView(packageNames, (i) -> (Component) ReflectUtil.newInstance(i));
    }

    /**
     * 扫描@ItemView 修饰的类,生成ItemNote 对象
     *
     * @param packageNames 包名如cn.note.swing
     * @return ItemNode的集合对象
     */
    public static List<ItemNode> scanAllItemView(String packageNames, DynamicComponent<Component> dynamicComponent) {
        Set<Class<?>> itemClasses = ClassUtil.scanPackageByAnnotation(packageNames, ItemView.class);
        List<ItemNode> itemSet = new ArrayList<>(itemClasses.size());
        for (Class<?> itemClass : itemClasses) {
            ItemNode itemNode = new ItemNode(itemClass, dynamicComponent);
            itemSet.add(itemNode);
        }
        itemSet.sort(Comparator.comparing(ItemNode::getOrder));
        return itemSet;
    }


}
