package com.mojave;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MojaveApplication {

    public static void main(String[] args) {
        SpringApplication.run(MojaveApplication.class, args);
    }
}
