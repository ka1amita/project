package com.gfa.utils;

public enum Endpoint {
    // don't add wildcards otherwise CustomAuthorizationFilter exceptions gonna break!
    REGISTER("/register"), CONFIRM_WITH_CODE("/confirm/"), LOGIN("/login"),
    RESET_PASSWORD("/reset"),
    VERIFY_EMAIL_WITH_TOKEN("/email/verify/"), RESEND_VERIFICATION_EMAIL("/email/verify/resend"),
    DASHBOARD("/dashboard"), REFRESH_TOKEN("/token/refresh"), HELLO_WORLD("/hello");
    private final String value;

    Endpoint(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}