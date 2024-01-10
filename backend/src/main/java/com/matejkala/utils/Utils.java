package com.matejkala.utils;

import org.springframework.security.core.Authentication;

import java.util.Random;
import java.util.stream.Collectors;

public class Utils {

    private static final String UserPasswordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!\"#$%&'()*+,-./:;<=>?@\\[\\]\\\\^_`{|}~]).{8,}$";
    private static final String ActivationCodeGenerationSample = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String GenerateActivationCode(Integer size) {
        if (size != null && size > 0) {
            Random random = new Random();
            return random.ints(0, ActivationCodeGenerationSample.length())
                    .limit(size)
                    .mapToObj(x -> String.valueOf(ActivationCodeGenerationSample.charAt(x)))
                    .collect(Collectors.joining());
        }
        return "";
    }

    public static Boolean IsUserPasswordFormatValid(String password) {
        if (isNotNullOrEmpty(password)) {
            return password.matches(UserPasswordPattern);
        }
        return false;
    }

    public static Boolean hasAdminRole(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> "ADMIN".equals(grantedAuthority.getAuthority()));
    }

    public static Boolean isNotNullOrEmpty(String field) {
        return field != null && !field.isEmpty();
    }
}
