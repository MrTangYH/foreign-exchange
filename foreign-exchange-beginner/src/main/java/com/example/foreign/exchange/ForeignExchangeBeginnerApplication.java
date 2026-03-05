package com.example.foreign.exchange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.example.foreign.exchange")
@EnableScheduling //定时任务
@EnableAsync //异步任务
public class ForeignExchangeBeginnerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ForeignExchangeBeginnerApplication.class, args);
    }

}
