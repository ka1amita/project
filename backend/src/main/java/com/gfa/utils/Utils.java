package com.gfa.utils;

import java.util.Random;
import java.util.stream.Collectors;

public class Utils {
    private static final String UserPasswordPattern = "^(?=.[a-z])(?=.[A-Z])(?=.\\d)(?=.[~!@#$%^&*()\\\\-_=+\\\\[\\\\]{}\\\\|;:'\",.<>/?]).[A-Za-z\\\\d~!@#$%^&*()\\-_=+\\[\\]{}\\|;:'\",.<>/?]{8,}$";
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
}
