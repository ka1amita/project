package com.gfa.utils;

import com.gfa.models.ActivationCode;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.stream.Collectors;

public class Utils {
    private static final String UserPasswordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    private static final String ActivationCodeGenerationSample = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    @Value("${ACTIVATION_CODE_EXPIRE_MINUTES:30}")
    private static final Integer ActivationCodeExpireMinutes = 30;
    @Value("${ACTIVATION_CODE_MAX_SIZE:48}")
    private static final Integer ActivationCodeMaxSize = 48;

    public static String GenerateActivationCode() {
        Random random = new Random();
        return random.ints(0, ActivationCodeGenerationSample.length())
                .limit(ActivationCodeMaxSize)
                .mapToObj(x -> String.valueOf(ActivationCodeGenerationSample.charAt(x)))
                .collect(Collectors.joining());
    }

    public static Boolean IsUserPasswordFormatValid(String password) {
        return password.matches(UserPasswordPattern);
    }

    public static Boolean IsActivationCodeValid(ActivationCode activationCode) {
        return activationCode.getCreatedAt().plusMinutes(ActivationCodeExpireMinutes).isAfter(LocalDateTime.now());
    }
}
