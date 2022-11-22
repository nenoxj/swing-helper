package cn.note.service.toolkit.eventbus;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.Test;

/**
 * @author jee
 * @version 1.0
 */
public class EventBusTest {


    @Test
    public void test() {
        EventBus eventLog = new EventBus("log");
        eventLog.register(new LogListener());


        eventLog.post("log 0");
        eventLog.post("log 1");
        eventLog.post("log 2");
        eventLog.post("log 3");
        eventLog.post("log 4");

        try {
            System.out.println(1 / 0);

        } catch (Exception e) {
            eventLog.post(e);
        }

        try {
            String a = new String[]{}[0];
        } catch (Exception e) {
            eventLog.post(e);
        }


    }


    class LogListener {
        @Subscribe
        public void listener(String log) {
            Console.log("{}---:{}", DateUtil.now(), log);

        }

        @Subscribe
        public void listener(Exception error) {
            Console.error("{}---:{} \n{}", DateUtil.now(), error.getMessage(), ExceptionUtils.getStackTrace(error));

        }
    }


}
