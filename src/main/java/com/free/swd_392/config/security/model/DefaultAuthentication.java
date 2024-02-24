package com.free.swd_392.config.security.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Collections;

/**
 * DefaultAuthentication
 */
public class DefaultAuthentication extends AbstractAuthenticationToken implements Authentication {
    /**
     * User details
     */
    private UserDetails principal;
    /**
     * Token
     */
    private String token;

    /**
     * @param principal {@link UserDetails}
     * @param token     token
     */
    public DefaultAuthentication(UserDetails principal, String token) {
        this(token);
        this.principal = principal;
    }

    /**
     * @param token token
     */
    public DefaultAuthentication(String token) {
        super(Collections.emptyList());
        Assert.hasText(token, "token cannot be empty");
        this.token = token;
    }

    /**
     * @param principal   {@link UserDetails}
     * @param token       token
     * @param authorities {@link GrantedAuthority}
     */
    public DefaultAuthentication(UserDetails principal, String token, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        Assert.hasText(token, "token cannot be empty");
        this.token = token;
        this.principal = principal;
    }

    @Override
    public Object getCredentials() {
        return getToken();
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    public String getToken() {
        return this.token;
    }

    public void setPrincipal(UserDetails principal) {
        this.principal = principal;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof DefaultAuthentication that)) return false;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(getPrincipal(), that.getPrincipal())
                .append(getToken(), that.getToken()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(getPrincipal())
                .append(getToken()).toHashCode();
    }
}
