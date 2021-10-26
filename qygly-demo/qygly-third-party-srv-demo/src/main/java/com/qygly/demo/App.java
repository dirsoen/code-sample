package com.qygly.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

import lombok.extern.slf4j.Slf4j;

/**
 *  */
@SpringBootApplication
@EnableEurekaClient
@ComponentScan(basePackages = {"com.qygly.demo", "com.qygly.ext.rest.helper"})
@Slf4j
@EnableFeignClients(basePackages = {"com.qygly.ext.rest.helper"})
public class App {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(App.class);
        app.run(args);
    }

}
