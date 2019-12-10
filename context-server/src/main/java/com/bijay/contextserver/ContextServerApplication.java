package com.bijay.contextserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ContextServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContextServerApplication.class, args);
    }

}
