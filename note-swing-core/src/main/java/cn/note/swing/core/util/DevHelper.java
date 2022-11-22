package cn.note.swing.core.util;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.ReflectUtil;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author jee
 * @version 1.0
 */
public class DevHelper {


    /**
     * @param clazz 打印类的属性值
     */
    public static void debugConstants(Class<?> clazz) {
        try {
            Map<String, Field> maps = ReflectUtil.getFieldMap(clazz);
            for (String key : maps.keySet()) {
                Field field = maps.get(key);
                Console.log("class:{}, fieldName:{},fieldValue:{}", clazz.getSimpleName(), key, field.get(clazz));
            }
        } catch (Exception e) {
            Console.error("class[{}]get Field Error: {}", clazz.getName(), e.getMessage());
        }

    }
}
