package com.free.swd_392.shared.utils;

import jakarta.annotation.Nullable;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class TypeUtils {

    @Nullable
    public static <S, T> T unwrap(S source) {
        return unwrap(source, false);
    }

    @SuppressWarnings("unchecked")
    public static <S, T> T unwrap(S source, boolean throwEx) {
        try {
            return (T) source;
        } catch (ClassCastException e) {
            if (throwEx) {
                log.warn("Cast cast error", e);
                throw e;
            }
            return null;
        }
    }
}
