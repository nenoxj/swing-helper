package cn.note.swing.core.lifecycle;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在方法上标记
 * 作为动态注册事件使用
 *
 * @author jee
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface InitAction {
    /**
     * @return 可以用作动作描述
     */
    public String value() default "";
}
