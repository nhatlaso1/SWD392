package com.free.swd_392.config.security.filter;

import com.free.swd_392.config.security.exception.DefaultAuthenticationException;
import com.free.swd_392.config.security.model.DefaultAuthentication;
import jakarta.annotation.Nonnull;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Bearer token filter
 */
public class BearerTokenFilter extends OncePerRequestFilter implements Filter {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(BearerTokenFilter.class);
    private final AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();
    private final AuthenticationProvider provider;
    private final BearerTokenResolver bearerTokenResolver = new DefaultBearerTokenResolver();
    private final RequestMatcher pathMatcher;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    /**
     * Filter on all request
     *
     * @param provider {@link AuthenticationProvider}
     */
    public BearerTokenFilter(AuthenticationProvider provider) {
        this(new BearerTokenAuthenticationEntryPoint(), provider, "/**");
    }

    /**
     * Filter on pattern
     *
     * @param provider    {@link AuthenticationProvider}
     * @param pathPattern path pattern
     */
    public BearerTokenFilter(AuthenticationEntryPoint authenticationEntryPoint, AuthenticationProvider provider, String pathPattern) {
        this.provider = provider;
        pathMatcher = new AntPathRequestMatcher(
                pathPattern
        );
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull FilterChain filterChain) throws ServletException, IOException {
        String token;
        try {
            token = bearerTokenResolver.resolve(request);
        } catch (OAuth2AuthenticationException invalid) {
            log.debug("Sending to authentication entry point since failed to resolve bearer token", invalid);
            authenticationEntryPoint.commence(request, response, new DefaultAuthenticationException());
            return;
        }
        if (token == null) {
            log.debug("Did not process request since did not find bearer token");
            filterChain.doFilter(request, response);
            return;
        }
        DefaultAuthentication authenticationRequest = new DefaultAuthentication(token);
        authenticationRequest.setDetails(authenticationDetailsSource.buildDetails(request));
        try {
            Authentication authenticationResult = provider.authenticate(authenticationRequest);
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authenticationResult);
            SecurityContextHolder.setContext(context);
        } catch (AuthenticationException failed) {
            log.debug("AuthenticationException {} {}", failed.getClass().getSimpleName(), failed.getMessage());
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Get path matcher
     *
     * @return {@link RequestMatcher}
     */
    protected RequestMatcher getPathMatcher() {
        return pathMatcher;
    }

    @Override
    protected boolean shouldNotFilter(@Nonnull HttpServletRequest request) {
        return !getPathMatcher().matches(request);
    }
}
