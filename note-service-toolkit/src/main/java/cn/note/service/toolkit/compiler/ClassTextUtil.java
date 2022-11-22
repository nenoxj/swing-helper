package cn.note.service.toolkit.compiler;

import cn.hutool.core.util.ClassUtil;

import java.lang.reflect.Method;

/**
 * // TODO
 *
 * @description:
 * @author: jee
 * @time: 2022/2/15 10:05
 */
public class ClassTextUtil {


    /**
     * 获得class路径
     *
     * @param clazz
     * @return
     */
    private static String getClassFile(Class<?> clazz) {
        String classPath = ClassUtil.getLocationPath(clazz);
        String packageName = ClassUtil.getPackagePath(clazz);
        String className = ClassUtil.getClassName(clazz, true);
        String classFilePath = classPath + packageName + "/" + className + ".class";
        return classFilePath;
    }

    /**
     * 获取class内容
     *
     * @param clazz
     * @return
     */
    public static String getClassContent(Class<?> clazz) {
        return DecompilerUtil.decompile(getClassFile(clazz));
    }


    /**
     * 获取方法内容
     *
     * @param method
     * @return
     */
    public static String getClassMethodContent(Method method) {
        return DecompilerUtil.decompile(getClassFile(method.getDeclaringClass()), method.getName());

    }
}
