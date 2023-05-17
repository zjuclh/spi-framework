package com.lingyun.components.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author caolianghong
 * @date 2022/8/11 2:50 下午
 */
@Configuration
public class SpiFrameworkConfiguration {
    @Bean
    SpiFrameworkBootstrap spiFrameworkBootstrap() {
        return new SpiFrameworkBootstrap();
    }
}
