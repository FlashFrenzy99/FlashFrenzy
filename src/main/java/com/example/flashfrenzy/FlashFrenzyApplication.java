package com.example.flashfrenzy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FlashFrenzyApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlashFrenzyApplication.class, args);
    }

}
