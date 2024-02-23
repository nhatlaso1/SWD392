package com.free.swd_392.shared.utils;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MapperUtil {

    private static final ObjectMapper mapper = new ObjectMapper()
            .enable(DeserializationFeature.UNWRAP_ROOT_VALUE)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .addHandler(
                    new DeserializationProblemHandler() {
                        @Override
                        public Object handleWeirdStringValue(DeserializationContext context, Class<?> targetType, String valueToConvert, String failureMsg) {
                            return null;
                        }

                        @Override
                        public Object handleWeirdNumberValue(DeserializationContext context, Class<?> targetType, Number valueToConvert, String failureMsg) {
                            return null;
                        }
                    }
            )
            .findAndRegisterModules();

    public static <T> T convertValue(Object fromValue, Class<T> toValueType) {
        return mapper.convertValue(fromValue, toValueType);
    }

    public static <T> T convertValue(Object fromValue, Class<T> toValueType, T defaultValue) {
        if (fromValue == null) {
            return defaultValue;
        }
        return mapper.convertValue(fromValue, toValueType);
    }
}
