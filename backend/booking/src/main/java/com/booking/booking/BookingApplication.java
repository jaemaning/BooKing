package com.booking.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;

import java.util.TimeZone;

@EnableEurekaClient
@SpringBootApplication
@EnableReactiveMongoAuditing
@EnableR2dbcAuditing
public class BookingApplication {
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        SpringApplication.run(BookingApplication.class, args);
    }
}
