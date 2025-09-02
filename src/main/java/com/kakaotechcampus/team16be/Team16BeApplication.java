package com.kakaotechcampus.team16be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@EnableJpaAuditing
@SpringBootApplication
public class Team16BeApplication {

    public static void main(String[] args) {
        SpringApplication.run(Team16BeApplication.class, args);
    }

}
