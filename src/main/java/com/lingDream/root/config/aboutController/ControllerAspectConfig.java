package com.lingDream.root.config.aboutController;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: 酷酷宅小明
 * @CreateTime: 2021-05-02 17:02
 */
@Configuration
@Data
public class ControllerAspectConfig {
    @Value("${lingDream.controller.aspect}")
    private boolean controllerBeforeAspectOnOff;
}
