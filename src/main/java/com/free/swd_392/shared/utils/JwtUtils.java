package com.free.swd_392.shared.utils;

import com.free.swd_392.config.security.model.DefaultUserDetails;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;

@UtilityClass
public class JwtUtils {

    public static Object getClaim(String key) {
        DefaultUserDetails details = getUserDetailsFromToken();
        Map<String, Object> claims = details.getClaims();
        return claims.get(key);
    }

    public static String getUserId() {
        return getUserDetailsFromToken().getSubject();
    }

    public static Long getMerchantId() {
        return (Long) getUserDetailsFromToken().getClaims().get("merchant_id");
    }

    public static String getSessionFromToken() {
        return getUserDetailsFromToken().getSessionState();
    }

    public static DefaultUserDetails getUserDetailsFromToken() {
        return (DefaultUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
