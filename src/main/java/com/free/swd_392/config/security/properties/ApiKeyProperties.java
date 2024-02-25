package com.free.swd_392.config.security.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties("spring.security")
public class ApiKeyProperties {

    private List<ApiKey> apiKey;

    @Data
    public static class ApiKey {

        private String path;
        private String key;
    }
}
