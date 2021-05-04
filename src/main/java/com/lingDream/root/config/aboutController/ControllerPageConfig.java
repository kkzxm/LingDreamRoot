package com.lingDream.root.config.aboutController;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: 酷酷宅小明
 * @CreateTime: 2021-05-02 19:17
 */
@Data
@Configuration
public class ControllerPageConfig {
    @Value("${lingDream.controller.listPage}")
    String listPage;
    @Value("${lingDream.controller.insertPage}")
    String insertPage;
    @Value("${lingDream.controller.resultPage}")
    String resultPage;
}
