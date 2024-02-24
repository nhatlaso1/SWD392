package com.free.swd_392.core.converter;

import com.free.swd_392.config.security.model.DefaultAuthentication;
import com.free.swd_392.config.security.model.DefaultUserDetails;
import com.free.swd_392.shared.utils.MapperUtil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class JwtConverter implements Converter<Jwt, DefaultAuthentication> {

    @Override
    public DefaultAuthentication convert(Jwt source) {
        DefaultUserDetails userDetails = MapperUtil.convertValue(source.getClaims(), DefaultUserDetails.class);
        userDetails.setClaims(source.getClaims());
        return new DefaultAuthentication(
                userDetails,
                source.getTokenValue()
        );
    }
}
