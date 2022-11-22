package cn.note.service.toolkit.script;

import cn.hutool.core.io.resource.ResourceUtil;
import org.junit.Test;

import java.nio.charset.Charset;

public class ScriptFactoryTest {

    @Test
    public void testGroovy() {
        String groovyScript = ResourceUtil.readStr("script/test.groovy", Charset.defaultCharset());
        ScriptFactory.invokeGroovyScript(groovyScript);
    }


    @Test
    public void testJs(){
        String jsScript = ResourceUtil.readStr("script/test.js", Charset.defaultCharset());
        ScriptFactory.invokeJsScript(jsScript);
    }
}