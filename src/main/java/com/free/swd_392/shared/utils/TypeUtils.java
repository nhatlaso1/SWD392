package com.free.swd_392.shared.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class TypeUtils {

    @SuppressWarnings("unchecked")
    public static <S, T> T unwrap(S source) throws ClassCastException {
        return (T) source;
    }
}
