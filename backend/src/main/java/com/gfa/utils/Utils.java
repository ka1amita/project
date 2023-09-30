package com.gfa.utils;

import com.gfa.exceptions.user.InvalidIdException;
import com.gfa.exceptions.user.MissingJSONBodyException;
import org.springframework.security.core.Authentication;

import java.util.Random;
import java.util.stream.Collectors;

public class Utils {
    private static final String UserPasswordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!\"#$%&'()*+,-./:;<=>?@\\[\\]\\\\^_`{|}~]).{8,}$";
    private static final String ActivationCodeGenerationSample = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String GenerateActivationCode(Integer size) {
        Random random = new Random();
        return random.ints(0, ActivationCodeGenerationSample.length())
                .limit(size)
                .mapToObj(x -> String.valueOf(ActivationCodeGenerationSample.charAt(x)))
                .collect(Collectors.joining());
    }

    public static Boolean IsUserPasswordFormatValid(String password) {
        return password.matches(UserPasswordPattern);
    }

    public static void IsProvidedIdValid(Long id) {
        if (id < 0) throw new InvalidIdException("Please provide a valid ID");
    }
    public static <T> void isJSONBodyPresent(T request) {
        if (request == null) throw new MissingJSONBodyException("Please provide a JSON body");
    }

    public static Boolean hasAdminRole(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> "ADMIN".equals(grantedAuthority.getAuthority()));
    }
    public static Boolean isNotNullOrEmpty(String field) {
        return field != null && !field.isEmpty();
    }
}
