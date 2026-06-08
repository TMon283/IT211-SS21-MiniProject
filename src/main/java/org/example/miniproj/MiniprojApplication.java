package org.example.miniproj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MiniprojApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiniprojApplication.class, args);
    }

}
