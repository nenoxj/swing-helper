package cn.note.swing.core.util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import cn.hutool.core.util.ClassUtil;
import org.slf4j.LoggerFactory;

import java.util.logging.Handler;
import java.util.logging.Logger;

/**
 * 控制日志级别工具类
 *
 * @author jee
 */
public class LoggerUtil {

    /**
     * 动态设置slf4级别
     *
     * @param packageName 包名
     * @param level       级别
     */
    public static void setLogLevel(String packageName, Level level) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.getLogger(packageName).setLevel(level);
    }

    /**
     * 级别为Info
     *
     * @param clazz 类
     */
    public static void setLogLevelInfo(Class<?> clazz) {
        setLogLevel(ClassUtil.getPackage(clazz), Level.INFO);
    }

    /**
     * 控制java.util.logger的级别
     */
    public static void adjustJavaLogLevel() {
        Logger logger = Logger.getLogger("");
        for (Handler handler : logger.getHandlers()) {
            handler.setLevel(java.util.logging.Level.SEVERE);
        }
    }
}
