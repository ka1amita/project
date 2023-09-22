package com.gfa.utils;

import com.gfa.exceptions.user.InvalidIdException;
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

    public static void IsUserIdValid(Long id) {
        if (id < 0) throw new InvalidIdException("Please provide a valid ID");
    }
}
