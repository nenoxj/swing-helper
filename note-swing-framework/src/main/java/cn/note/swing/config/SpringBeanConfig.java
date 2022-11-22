package cn.note.swing.config;

import cn.note.swing.SpringViewApplication;
import lombok.extern.slf4j.Slf4j;
import org.jdesktop.application.Application;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * spring配置
 */
@Slf4j
@Configuration
@ComponentScan(basePackages = {"cn.note.swing", "cn.note.service"})
public class SpringBeanConfig {

    /**
     * 与basePackages中的包名一致
     */
    public static final String PACKAGE_NAME = "cn.note.swing";

    /**
     * @return 上下文管理对象, 可以在component中直接调用父组件
     */
    @Bean
    public SpringViewApplication application() {
        return (SpringViewApplication) Application.getInstance();
    }

}
