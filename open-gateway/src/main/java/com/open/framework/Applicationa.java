package com.open.framework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;

@SpringBootApplication
public class Applicationa {

    public static void main(String[] args) {
        SpringApplication springApplication =new SpringApplication(Applicationa.class);
        springApplication.run(args);
    }
}