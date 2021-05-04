package com.lingDream.root.config.aboutService;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 业务层环绕通知开关
 * @Author: 酷酷宅小明
 * @CreateTime: 2021-05-02 17:02
 */
@Configuration
@Data
public class ServiceAspectConfig {
    @Value("${lingDream.service.aspect.insert}")
    private boolean serviceInsertAspectOnOff = false;
    @Value("${lingDream.service.aspect.update}")
    private boolean serviceUpdateAspectOnOff = false;
}
