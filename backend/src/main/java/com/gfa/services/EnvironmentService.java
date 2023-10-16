package com.gfa.services;

import javax.servlet.http.HttpServletRequest;

public interface EnvironmentService {
    String getEnv();

    String getServerName(HttpServletRequest request);
}
