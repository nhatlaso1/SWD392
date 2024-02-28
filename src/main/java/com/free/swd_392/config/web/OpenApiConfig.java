package com.free.swd_392.config.web;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.GlobalOperationCustomizer;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Objects;

@Configuration(proxyBeanMethods = false)
@SecuritySchemes(
        value = {
                @SecurityScheme(
                        name = "Authorization",
                        type = SecuritySchemeType.HTTP,
                        bearerFormat = "Bearer [token]",
                        scheme = "bearer",
                        in = SecuritySchemeIn.HEADER,
                        description = "Access token"
                ),
                @SecurityScheme(
                        name = "x-api-key",
                        type = SecuritySchemeType.APIKEY,
                        in = SecuritySchemeIn.HEADER
                )
        }
)
@OpenAPIDefinition(
        info = @Info(title = "SWD_392 Auction API", version = "1.0.0", description = "API documentation of SWD_392 Auction v1.0.0"),
        security = @SecurityRequirement(name = "Authorization")
)
@RequiredArgsConstructor
public class OpenApiConfig {

    @Value("#{'${springdoc.server-urls}'.split(',')}")
    private final List<String> serverUrls;

    @Bean
    @ConditionalOnMissingBean
    public OpenAPI openAPI() {
        OpenAPI openAPI = new OpenAPI();
        if (serverUrls == null) {
            return openAPI;
        }
        return openAPI.servers(serverUrls.stream().map(s -> new Server().url(s)).toList());
    }
    @Bean
    @ConditionalOnMissingBean
    public GlobalOperationCustomizer operationIdCustomizer() {
        return (operation, handlerMethod) -> {
            Class<?> superClazz = handlerMethod.getBeanType().getSuperclass();
            if (Objects.nonNull(superClazz)) {
                String beanName = handlerMethod.getBeanType().getSimpleName();
                operation.setOperationId(String.format("%s_%s", beanName, handlerMethod.getMethod().getName()));
            }
            return operation;
        };
    }

}
