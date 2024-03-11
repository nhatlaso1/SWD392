package com.free.swd_392.shared.utils;

import lombok.experimental.UtilityClass;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@UtilityClass
public class PasswordUtils {

    public static String encodeBasicCredential(String username, String password) {
        String input = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(input.getBytes(StandardCharsets.UTF_8));
    }
}
