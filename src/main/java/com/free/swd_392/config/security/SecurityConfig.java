package com.free.swd_392.config.security;

import com.free.swd_392.config.security.filter.ApiKeyFilter;
import com.free.swd_392.config.security.filter.BearerTokenFilter;
import com.free.swd_392.config.security.properties.ApiKeyProperties;
import com.free.swd_392.config.security.properties.IgnoreAuthorizationProperties;
import com.free.swd_392.core.converter.JwtConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.free.swd_392.enums.RoleKind.*;

@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
@RequiredArgsConstructor
@EnableConfigurationProperties({IgnoreAuthorizationProperties.class, ApiKeyProperties.class})
public class SecurityConfig {

    private final IgnoreAuthorizationProperties ignoreAuthorizationProperties;
    private final ApiKeyProperties apiKeyProperties;
    private final JwtDecoder jwtDecoder;
    private final JwtConverter jwtConverter;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        var jwtAuthProvider = new JwtAuthProvider(jwtDecoder, jwtConverter);
        if (apiKeyProperties != null) {
            for (var apiKey : apiKeyProperties.getApiKey()) {
                http.addFilterAfter(new ApiKeyFilter(apiKey.getPath(), apiKey.getKey()), LogoutFilter.class);
            }
        }
        return http.csrf(AbstractHttpConfigurer::disable)
                .cors(this::configCors)
                .authorizeHttpRequests(
                        customizer -> customizer
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                .requestMatchers(ignoreAuthorizationProperties.getIgnoreAuthorization().toArray(String[]::new)).permitAll()
                                .requestMatchers("/api/v1/app/product/**").hasRole(MERCHANT_VALUE)
                                .requestMatchers("/api/v1/app/product-sku/**").hasRole(MERCHANT_VALUE)
                                .requestMatchers("/api/v1/system/**").hasRole(CMS_VALUE)
                                .requestMatchers("/api/v1/system/user/**").hasRole(SUPER_ADMIN_VALUE)
                                .requestMatchers("/api/v1/system/product-category/**").hasRole(SUPER_ADMIN_VALUE)
                                .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2Configurer -> oauth2Configurer
                        .jwt(jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(new JwtConverter()))
                )
                .addFilterAfter(new BearerTokenFilter(jwtAuthProvider), LogoutFilter.class)
                .authenticationProvider(jwtAuthProvider)
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

    @Bean
    public RoleHierarchy customRoleHierarchy() {
        Map<String, List<String>> roleHierarchyMapping = new HashMap<>();
        roleHierarchyMapping.put(MERCHANT_VALUE, List.of(USER_VALUE));
        roleHierarchyMapping.put(SUPER_ADMIN_VALUE, List.of(CMS_VALUE));
        return roleHierarchyFromMap(roleHierarchyMapping);
    }

    @Bean
    public DefaultWebSecurityExpressionHandler customWebSecurityExpressionHandler(@Qualifier("customRoleHierarchy") RoleHierarchy roleHierarchy) {
        DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy);
        return expressionHandler;
    }

    private void configCors(CorsConfigurer<HttpSecurity> cors) {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOriginPatterns(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(List.of("Origin", "Authorization", "Cache-Control", "Content-Type", "Accept", "Accept-Encoding", "X-Requested-With", "remember-me"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        cors.configurationSource(source);
    }

    private static RoleHierarchy roleHierarchyFromMap(Map<String, List<String>> roleHierarchyMapping) {

        StringWriter roleHierarchyDescriptionBuffer = new StringWriter();
        PrintWriter roleHierarchyDescriptionWriter = new PrintWriter(roleHierarchyDescriptionBuffer);

        for (Map.Entry<String, List<String>> entry : roleHierarchyMapping.entrySet()) {
            String currentRole = concatPrefix(entry.getKey());
            List<String> impliedRoles = entry.getValue();
            for (String impliedRole : impliedRoles) {
                String roleMapping = currentRole + " > " + concatPrefix(impliedRole);
                roleHierarchyDescriptionWriter.println(roleMapping);
            }
        }
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy(roleHierarchyDescriptionBuffer.toString());
        return roleHierarchy;
    }
}
