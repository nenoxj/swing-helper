package cn.note.service.toolkit.script;

import cn.hutool.core.util.ArrayUtil;
import lombok.extern.slf4j.Slf4j;

import javax.script.*;
import java.util.List;
import java.util.Map;

/**
 * @author jee
 * @description: 脚本调用工具
 */
@Slf4j
public class ScriptInvoke {

    private ScriptEngine engine = null;

    private ScriptInvoke(String name) {
        engine = new ScriptEngineManager().getEngineByName(name);
    }

    public static ScriptInvoke instance(ScriptType scriptType) {
        return new ScriptInvoke(scriptType.name());
    }

    /**
     * 执行脚本
     *
     * @param script 脚本内容
     */
    public void invokeScript(String script) {
        invokeGetScript(script, null, null, null);
    }


    /**
     * 执行脚本
     *
     * @param script    脚本内容
     * @param variables 变量参数
     */
    public void invokeScript(String script, Map<String, Object> variables) {
        invokeGetScript(script, variables, null, null);
    }

    /**
     * 执行脚本方法并获取结果
     *
     * @param script 脚本内容
     * @param func   方法
     * @param args   方法参数
     * @return 执行结果
     */
    public Object invokeGetScript(String script, String func, Object[] args) {
        return invokeGetScript(script, null, func, args);
    }

    /**
     * @param script 脚本内容
     * @param func   方法
     * @param args   方法参数
     * @return 执行结果
     */
    public Object invokeGetScript(String script, String func, List<Object> args) {
        return invokeGetScript(script, null, func, ArrayUtil.toArray(args, Object.class));
    }

    /**
     * @param script    脚本内容
     * @param variables 注入变量
     * @param func      方法
     * @param args      参数
     * @return 执行结果
     */
    public Object invokeGetScript(String script, Map<String, Object> variables, String func, Object[] args) {
        try {
            // 是否传递变量
            if (variables != null) {
                Bindings bindings = engine.createBindings();
                bindings.putAll(variables);
                engine.eval(script, bindings);
            } else {
                engine.eval(script);
            }
            if (func != null) {
                // 获取执行结果
                Invocable invocable = (Invocable) engine;
                Object scriptResult = null;
                // 无参方法
                if (args == null) {
                    scriptResult = invocable.invokeFunction(func);
                } else {
                    scriptResult = invocable.invokeFunction(func, args);
                }
                log.info("执行脚本结果：{}", scriptResult);
                return scriptResult;
            }

        } catch (ScriptException e) {
            log.error("执行脚本异常！", e);
        } catch (NoSuchMethodException e) {
            log.error("脚本未实现{}方法", e, func);
        }
        return null;
    }
}
