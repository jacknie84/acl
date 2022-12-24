package com.jacknie.examples.acl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.jacknie.examples.acl.test")
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
