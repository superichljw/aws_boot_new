package com.example.aws_boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

//@EnableJpaAuditing  //jpa auditing activate
@SpringBootApplication
public class AwsBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(AwsBootApplication.class, args);
    }

}
