package com.jenkin.sometools;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages = "com.jenkin")
@MapperScan({"com.jenkin.*.dao"})
@MapperScan({"com.jenkin.*.*.dao"})
//@EnableFeignClients(basePackages = "com.jenkin.sometools")
@EnableSwagger2
public class SometoolsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SometoolsApplication.class, args);
    }

}
