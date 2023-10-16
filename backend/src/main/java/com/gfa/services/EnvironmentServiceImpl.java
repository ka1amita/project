package com.gfa.services;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class EnvironmentServiceImpl implements EnvironmentService {

    @Override
    public String getEnv() {
        return System.getenv("ENV");
    }

    @Override
    public String getServerName(HttpServletRequest request) {
        return request.getServerName();
    }
}
