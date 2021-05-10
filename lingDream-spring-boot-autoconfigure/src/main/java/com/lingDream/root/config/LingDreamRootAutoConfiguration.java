package com.lingDream.root.config;

import com.lingDream.root.aspect.ControllerAspect;
import com.lingDream.root.aspect.LingDreamConfig;
import com.lingDream.root.aspect.ServiceBefore;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.Ordered;

/**
 * @Author: 酷酷宅小明
 * @CreateTime: 2021-05-10 09:41
 */
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnClass(LingDreamConfig.class)
@ConditionalOnWebApplication(type = Type.SERVLET)
@EnableConfigurationProperties({ControllerAspect.class,ServiceBefore.class})
public class LingDreamRootAutoConfiguration {
}
