package com.lingDream.root.aspect;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @Author: 酷酷宅小明
 * @CreateTime: 2021-05-02 19:17
 */
@Data
@ConfigurationProperties(prefix = "lingDream", ignoreUnknownFields = true)
public class LingDreamConfig {
    @NestedConfigurationProperty
    private Controller controller = new Controller();
    @NestedConfigurationProperty
    private Service service = new Service();

    @Data
    static class Controller {
        String listPage;
        String insertPage;
        String resultPage;
        boolean aspect;
    }

    @Data
    static class Service {
        private boolean serviceInsertAspectOnOff;
        private boolean serviceUpdateAspectOnOff;
    }

}

