package com.free.swd_392.config.security.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;

@Data
@SuperBuilder(toBuilder = true)
public class DefaultGrantedAuthority implements GrantedAuthority {
    private String authority;
}
