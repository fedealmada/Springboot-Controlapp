package com.grupoplazaverde;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.grupoplazaverde")

public class ControlappApplication {
    public static void main(String[] args) {
        SpringApplication.run(ControlappApplication.class, args);
    }
}
