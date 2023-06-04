package com.morotech.bookqualifier.book.domain.common;

public class DomainException extends RuntimeException {

    private DomainException(String errorMessage) {
        super(errorMessage);
    }

    public static void whenValueIsNull(Integer value, String errorMessage) {
        if (value == null) {
            thenThrow(errorMessage);
        }
    }

    public static void whenValueIsNull(String value, String errorMessage) {
        if (value == null) {
            thenThrow(errorMessage);
        }
    }

    public static void whenValueEqualOrBellowZero(Integer value, String errorMessage) {
        if (value <= 0) {
            thenThrow(errorMessage);
        }
    }

    public static void whenValueIsBelowZeroOrAboveFive(Integer value, String errorMessage) {
        if (value < 0 || value > 5) {
            thenThrow(errorMessage);
        }
    }

    public static void whenTrue(boolean value, String errorMessage) {
        if (value) {
            thenThrow(errorMessage);
        }
    }

    public static void whenValueIsEmpty(String value, String errorMessage) {
        if (value.isEmpty()) {
            thenThrow(errorMessage);
        }
    }

    public static void whenValueIsBlank(String value, String errorMessage) {
        if (value.isBlank()) {
            thenThrow(errorMessage);
        }
    }

    private static void thenThrow(String errorMessage) {
        throw new DomainException(errorMessage);
    }
}
