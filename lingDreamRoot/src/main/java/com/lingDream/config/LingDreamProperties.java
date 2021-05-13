package com.lingDream.config;

import com.lingDream.config.controller.Controller;
import com.lingDream.config.service.Service;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
/**
 * 属性绑定类
 * @Author: 酷酷宅小明
 * @CreateTime: 2021-05-10 12:02
 */
@Data
@Accessors(chain = true)
@ConfigurationProperties(prefix = "ling-dream", ignoreUnknownFields = true)
public class LingDreamProperties {
    @NestedConfigurationProperty
    private Controller controller = new Controller();
    @NestedConfigurationProperty
    private Service service = new Service();
}

