package com.lingDream.root.config;

import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//Spring boot方式

/**
 * 备用
 * 开启mybatis-plus分页的bean,
 * 需要用时直接复制,粘贴到自己项目即可
 */
@Configuration
public class MybatisPlusConfig {
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}