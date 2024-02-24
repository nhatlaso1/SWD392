package com.free.swd_392.config.web;

import com.fasterxml.jackson.databind.MapperFeature;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class WebCommonConfiguration {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonFeatureCustomizer() {
        return builder -> builder
                .featuresToEnable(MapperFeature.DEFAULT_VIEW_INCLUSION);
    }
}
