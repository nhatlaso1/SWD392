package com.free.swd_392.config.firebase;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.firebase")
public class FirebaseConfigurationProperties {

    private String projectName;
    private String configPath;
}
