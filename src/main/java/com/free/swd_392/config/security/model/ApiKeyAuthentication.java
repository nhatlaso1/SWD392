package com.free.swd_392.config.security.model;

import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashSet;

/**
 * Api key authentication
 */
public class ApiKeyAuthentication implements Authentication {
    /**
     * Key
     */
    @Getter
    private final String key;
    /**
     * Pattern
     */
    @Getter
    private final String pattern;

    /**
     * authenticated
     */
    private boolean isAuthenticated;

    /**
     * Constructor
     *
     * @param pattern pattern
     * @param key     key
     */
    public ApiKeyAuthentication(String pattern, String key) {
        this.key = key;
        this.pattern = pattern;
        this.isAuthenticated = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new HashSet<>();
    }

    @Override
    public Object getCredentials() {
        return getKey();
    }

    @Override
    public Object getDetails() {
        return getCredentials();
    }

    @Override
    public Object getPrincipal() {
        return getKey();
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.isAuthenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return pattern;
    }

}
