package com.lingDream.config;

import com.lingDream.config.aspect.ControllerAspect;
import com.lingDream.config.aspect.ServiceBefore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * 自动配置类
 * @Author: 酷酷宅小明
 * @CreateTime: 2021-05-10 09:41
 */
@Configuration
@EnableConfigurationProperties(LingDreamProperties.class)
@ConditionalOnWebApplication(type = Type.SERVLET)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
public class LingDreamAutoConfiguration {
    @Autowired
    LingDreamProperties lingDreamProperties;

    @Bean
    public LingDreamProperties lingDreamProperties() {
        return lingDreamProperties;
    }

    @Bean
    @ConditionalOnMissingBean(ServiceBefore.class)
    public ServiceBefore serviceBefore(){
        return new ServiceBefore(lingDreamProperties);
    }

    @Bean
    @ConditionalOnMissingBean(ControllerAspect.class)
    public ControllerAspect controllerAspect(){
        return new ControllerAspect(lingDreamProperties);
    }
}
