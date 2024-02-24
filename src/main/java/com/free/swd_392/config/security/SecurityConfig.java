package com.free.swd_392.config.security;

import com.free.swd_392.config.security.properties.IgnoreAuthorizationProperties;
import com.free.swd_392.core.converter.JwtConverter;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration(proxyBeanMethods = false)
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true)
@RequiredArgsConstructor
@SecuritySchemes(
        value = {
                @SecurityScheme(
                        name = "Authorization",
                        type = SecuritySchemeType.HTTP,
                        bearerFormat = "Bearer [token]",
                        scheme = "bearer",
                        in = SecuritySchemeIn.HEADER,
                        description = "Access token"
                )
        }
)
@OpenAPIDefinition(
        info = @Info(title = "SWD_392 Auction API", version = "1.0.0", description = "API documentation of SWD_392 Auction v1.0.0"),
        security = @SecurityRequirement(name = "Authorization")
)
@EnableConfigurationProperties(IgnoreAuthorizationProperties.class)
public class SecurityConfig {

    private final IgnoreAuthorizationProperties ignoreAuthorizationProperties;
    private final JwtDecoder jwtDecoder;
    private final JwtConverter jwtConverter;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .cors(this::configCors)
                .authorizeHttpRequests(
                        customizer -> customizer
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                .requestMatchers(ignoreAuthorizationProperties.getIgnoreAuthorization().toArray(String[]::new)).permitAll()
                                .anyRequest().authenticated()

                )
                .oauth2ResourceServer(oauth2Configurer -> oauth2Configurer
                        .jwt(jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(new JwtConverter()))
                )
                .authenticationProvider(new JwtAuthProvider(jwtDecoder, jwtConverter))
                .exceptionHandling(exCustomizer -> exCustomizer.authenticationEntryPoint(restAuthenticationEntryPoint))
                .logout(logoutConfigurer -> logoutConfigurer
                        .logoutUrl("/api/v1/auth/logout")
                        .permitAll()
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                )
                .build();

    }

    private void configCors(CorsConfigurer<HttpSecurity> cors) {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(List.of("Origin", "Authorization", "Cache-Control", "Content-Type", "Accept", "Accept-Encoding", "X-Requested-With", "remember-me"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        cors.configurationSource(source);
    }
}
