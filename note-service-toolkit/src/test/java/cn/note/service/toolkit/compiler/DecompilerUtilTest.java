package cn.note.service.toolkit.compiler;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import org.junit.Test;

import java.lang.reflect.Method;

public class DecompilerUtilTest {

    @Test
    public void decompile() {

        Class<?> class1 = DecompilerUtil.class;
        String classPath = ClassUtil.getLocationPath(class1);
        String packageName = ClassUtil.getPackagePath(class1);
        String className = ClassUtil.getClassName(class1, true);
        String classFilePath = classPath + packageName + "/" + className + ".class";
        Console.log(classFilePath);
        Console.log("--------------------------get class ------------------------------------------");
        String classText = DecompilerUtil.decompile(classFilePath);
        Console.log(classText);

        Console.log("--------------------------get method ------------------------------------------");
        String methodText = DecompilerUtil.decompile(classFilePath, "decompileWithMappings");
        Console.log(methodText);
    }


    /**
     * 测试反编译
     */
    @Test
    public void decompile2() {


//        System.out.println(ClassTextUtil.getClassContent(DecompilerUtilTest.class));
        Method[] methods = ReflectUtil.getMethodsDirectly(DecompilerUtilTest.class, false);
        for (Method method : methods) {
            Console.log("--------------{}----------------", method.getName());
            Console.log(ClassTextUtil.getClassMethodContent(method));
        }

    }
}