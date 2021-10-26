package com.qygly.demo.ext.debug;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.qygly.ext.jar.helper.debug")
public class DebugDemoExt {
    private static final Logger log = LoggerFactory.getLogger(DebugDemoExt.class);

    public DebugDemoExt() {
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(new Class[]{DebugDemoExt.class});
        app.run(args);
    }
}