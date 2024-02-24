package com.free.swd_392.config;

import com.free.swd_392.config.security.model.ApiKeyAuthentication;
import com.free.swd_392.config.security.model.DefaultUserDetails;
import com.free.swd_392.shared.utils.JwtUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class AuditConfig {

    private static final String API_KEY_AUDIT = "api-key";

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(authentication -> {
                    if (authentication.getPrincipal() instanceof DefaultUserDetails) {
                        return JwtUtils.getUserId();
                    } else if (authentication instanceof ApiKeyAuthentication) {
                        return API_KEY_AUDIT;
                    }
                    return null;
                });
    }
}
