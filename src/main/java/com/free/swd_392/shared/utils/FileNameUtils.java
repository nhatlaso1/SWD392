package com.free.swd_392.shared.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.RandomStringUtils;

@UtilityClass
public class FileNameUtils {

    public static String generateFileName() {
        return RandomStringUtils.randomAlphanumeric(20) + "_" + System.currentTimeMillis();
    }
}
