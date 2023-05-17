package com.lingyun.components;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author caolianghong
 * @date 2022/8/11 2:53 下午
 */
@SpringBootApplication(scanBasePackages = "com.duowan.components")
public class TestApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }
}
