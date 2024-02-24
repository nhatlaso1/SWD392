package com.free.swd_392.config.security;

import com.free.swd_392.config.security.model.DefaultAuthentication;
import com.free.swd_392.core.converter.JwtConverter;
import com.free.swd_392.exception.InvalidException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.util.Assert;

@Slf4j
public class JwtAuthProvider implements AuthenticationProvider {

    private final JwtDecoder jwtDecoder;
    private final JwtConverter jwtConverter;

    public JwtAuthProvider(JwtDecoder jwtDecoder, JwtConverter jwtConverter) {
        Assert.notNull(jwtDecoder, "jwtDecoder cannot be null");
        this.jwtDecoder = jwtDecoder;
        this.jwtConverter = jwtConverter;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        DefaultAuthentication bearer = (DefaultAuthentication) authentication;
        Jwt jwt = getJwt(bearer);
        DefaultAuthentication token = jwtConverter.convert(jwt);
        if (token == null) {
            throw new InvalidException("Invalid Token");
        }
        token.setDetails(bearer.getDetails());
        log.debug("Authenticated token");
        return token;
    }

    private Jwt getJwt(DefaultAuthentication bearer) {
        try {
            return jwtDecoder.decode(bearer.getToken());
        } catch (BadJwtException failed) {
            log.debug("Failed to authenticate since the JWT was invalid");
            throw new InvalidBearerTokenException(failed.getMessage(), failed);
        } catch (JwtException failed) {
            throw new AuthenticationServiceException(failed.getMessage(), failed);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return DefaultAuthentication.class.isAssignableFrom(authentication);
    }
}
