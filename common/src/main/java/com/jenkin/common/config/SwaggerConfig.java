package com.jenkin.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author huangdj
 */
@Configuration
@EnableSwagger2
@Profile({"dev","test"})
public class SwaggerConfig {
}
