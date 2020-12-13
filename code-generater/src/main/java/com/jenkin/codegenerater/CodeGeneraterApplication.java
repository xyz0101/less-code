package com.jenkin.codegenerater;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages = "com.jenkin")
@MapperScan({"com.jenkin.*.dao"})
@MapperScan({"com.jenkin.*.*.dao"})
@EnableSwagger2
public class CodeGeneraterApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodeGeneraterApplication.class, args);
    }

}
