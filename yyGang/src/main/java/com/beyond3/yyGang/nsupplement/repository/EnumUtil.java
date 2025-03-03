package com.beyond3.yyGang.nsupplement.repository;

import com.beyond3.yyGang.exception.InvalidEnumValueException;
import com.beyond3.yyGang.exception.message.ExceptionMessage;

import java.util.Arrays;

public class EnumUtil {
    public static <T extends Enum<T>> T getEnumValue(Class<T> enumClass, String value) {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> e.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new InvalidEnumValueException(ExceptionMessage.INVALID_ENUM_VALUE));
    }
}
