package cn.note.service.toolkit.script;

/**
 * 脚本工厂
 */
public class ScriptFactory {
    /**
     * @return 生成groovy执行引擎
     */
    public static ScriptInvoke groovyScript() {
        return ScriptInvoke.instance(ScriptType.groovy);
    }

    /**
     * @return 生成js执行引擎
     */
    public static ScriptInvoke jsScript() {
        return ScriptInvoke.instance(ScriptType.javascript);
    }


    /**
     * 简单的执行groovy脚本
     *
     * @param groovyScript groovy脚本
     */
    public static void invokeGroovyScript(String groovyScript) {
        groovyScript().invokeScript(groovyScript);
    }

    /**
     * 简单的执行js脚本
     *
     * @param jsScript js脚本
     */
    public static void invokeJsScript(String jsScript) {
        jsScript().invokeScript(jsScript);
    }
}
