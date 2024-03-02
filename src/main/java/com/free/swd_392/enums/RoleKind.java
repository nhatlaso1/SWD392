package com.free.swd_392.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RoleKind {

    CMS,
    SUPER_ADMIN,
    USER,
    MERCHANT;

    public static final String CMS_VALUE = "CMS";
    public static final String SUPER_ADMIN_VALUE = "SUPER_ADMIN";
    public static final String USER_VALUE = "USER";
    public static final String MERCHANT_VALUE = "MERCHANT";

    public static String concatPrefix(String authority) {
        return "ROLE_" + authority;
    }
}
