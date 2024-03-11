package com.free.swd_392.config.client.paypal;

import com.free.swd_392.config.client.paypal.response.AccessTokenResponse;
import com.free.swd_392.shared.utils.PasswordUtils;
import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
public class PayPalClientConfig {

    @Lazy
    private final PayPalClient payPalClient;
    @Value("${paypal.client-id}")
    private final String paypalClientId;
    @Value("${paypal.secret-key}")
    private final String paypalSecretKey;
    private AtomicReference<AccessTokenResponse> accessToken;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            if (!template.path().contains("/v1/oauth2/token")) {
                template.header(HttpHeaders.AUTHORIZATION, getToken());
            }
        };
    }

    private String getToken() {
        if (accessToken == null
                || accessToken.get() == null
                || accessToken.get().getExpiration().isBefore(Instant.now().minusSeconds(30))) {
            var newAccessToken = payPalClient.getAccessToken(PasswordUtils.encodeBasicCredential(paypalClientId, paypalSecretKey));
            newAccessToken.setExpiration(Instant.now().plusSeconds(newAccessToken.getExpiresIn()));
            accessToken = new AtomicReference<>();
            accessToken.set(newAccessToken);
        }
        return accessToken.get().getTokenType() + " " + accessToken.get().getAccessToken();
    }

}
