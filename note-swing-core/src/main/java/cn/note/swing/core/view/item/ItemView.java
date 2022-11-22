package cn.note.swing.core.view.item;

import cn.note.swing.core.view.icon.SvgIconFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author jee
 * 菜单构建注解
 * 通过category识别分类 和类路径
 * 作为左侧折叠菜单区域的的注册使用
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ItemView {

    /**
     * 名称
     */
    String value();


    /**
     * 分类 --默认不分组
     */
    String category() default "";


    /**
     * 排序
     */
    int order() default 10000;


    /**
     * 图标 --默认为透明空图标
     */
    String icon() default SvgIconFactory.Common.transparent;

    /**
     * 描述
     */
    String description() default "";
}
