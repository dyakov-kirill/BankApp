package ru.dyakov.utils;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

public class PhoneNumberValidator {
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^\\d{11}$");

    public static boolean isValid(String phoneNumber) {
        if (phoneNumber == null) {
            return false;
        }
        return PHONE_NUMBER_PATTERN.matcher(phoneNumber).matches();
    }
}
