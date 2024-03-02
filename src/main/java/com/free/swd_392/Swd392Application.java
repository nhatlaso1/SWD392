package com.free.swd_392;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy(proxyTargetClass = true)
@SpringBootApplication
public class Swd392Application {

    public static void main(String[] args) {
        SpringApplication.run(Swd392Application.class, args);
    }

}
