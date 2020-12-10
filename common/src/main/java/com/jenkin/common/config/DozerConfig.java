package com.jenkin.common.config;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jenkin
 * @className DozerConfig
 * @description 
 * @date 2020/6/19 9:58
 */
@Configuration
public class DozerConfig {
    @Bean
    public Mapper mapper(){
        Mapper mapper = DozerBeanMapperBuilder.create().withMappingFiles("dozer-conveter.xml").build();
        return mapper;
    }
}
