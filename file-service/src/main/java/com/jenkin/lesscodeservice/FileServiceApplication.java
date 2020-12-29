package com.jenkin.lesscodeservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@MapperScan({"com.jenkin.*.dao"})
@MapperScan({"com.jenkin.*.*.dao"})
@SpringBootApplication(scanBasePackages = "com.jenkin")
@EnableFeignClients(basePackages = "com.jenkin.lesscodeservice")
public class FileServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileServiceApplication.class, args);
    }

}
