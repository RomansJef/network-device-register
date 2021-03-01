package com.register;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Device Register App.
 */
@SpringBootApplication
@EnableSwagger2
public class DeviceRegisterApp {
    public static void main(String[] args) {
        SpringApplication.run(DeviceRegisterApp.class, args);
    }
}
