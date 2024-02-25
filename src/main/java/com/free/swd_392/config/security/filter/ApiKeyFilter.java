package com.free.swd_392.config.security.filter;

import com.free.swd_392.config.security.model.ApiKeyAuthentication;
import jakarta.annotation.Nonnull;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Api key filter
 */
public class ApiKeyFilter extends OncePerRequestFilter implements Filter {
    private static final String API_KEY = "x-api-key";

    private final RequestMatcher matcher;
    private final String pattern;
    private final String key;

    public ApiKeyFilter(String pattern, String key) {
        this.pattern = pattern;
        this.key = key;
        matcher = new AntPathRequestMatcher(pattern);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull FilterChain filterChain) throws ServletException, IOException {
        String requestKey = request.getHeader(API_KEY);
        if (requestKey == null || !requestKey.equals(key)) {
            filterChain.doFilter(request, response);
            return;
        }
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authenticationResult = new ApiKeyAuthentication(pattern, key);
        context.setAuthentication(authenticationResult);
        SecurityContextHolder.setContext(context);
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(@Nonnull HttpServletRequest request) {
        return !getMatcher().matches(request);
    }

    protected RequestMatcher getMatcher() {
        return matcher;
    }
}
