package com.gfa.utils;

public interface Endpoint {
    // don't add wildcards otherwise CustomAuthorizationFilter exceptions gonna break!
    String HELLO_WORLD="/hello";
    String REGISTER = "/register";
    String CONFIRM_WITH_CODE = "/confirm";
    String LOGIN = "/login";
    String RESET_PASSWORD="/reset";
    String VERIFY_EMAIL_WITH_TOKEN="/email/verify";
    String RESEND_VERIFICATION_EMAIL="/email/verify/resend";
    String DASHBOARD="/dashboard";
    String USERS_API ="/api/users";
    String REFRESH_TOKEN="/token/refresh";

}