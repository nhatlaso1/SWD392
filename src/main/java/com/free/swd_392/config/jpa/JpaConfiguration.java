package com.free.swd_392.config.jpa;

import com.free.swd_392.config.security.model.ApiKeyAuthentication;
import com.free.swd_392.config.security.model.DefaultUserDetails;
import com.free.swd_392.shared.utils.JwtUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.envers.repository.config.EnableEnversRepositories;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableEnversRepositories
@EnableJpaRepositories(
        basePackages = "com.free.swd_392.repository",
        repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class
)
public class JpaConfiguration {

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
