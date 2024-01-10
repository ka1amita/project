package com.matejkala.filters;

import com.matejkala.config.RibbonProperties;
import com.matejkala.services.EnvironmentService;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class RibbonFilter extends OncePerRequestFilter {

    private final RibbonProperties ribbonProperties;
    private final EnvironmentService environmentService;

    @Autowired
    public RibbonFilter(RibbonProperties ribbonProperties, EnvironmentService environmentService) {
        this.ribbonProperties = ribbonProperties;
        this.environmentService = environmentService;
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain)
        throws ServletException, IOException {

        if (ribbonProperties.isEnabled()) {
            response.setHeader("Environment", environmentService.getEnv());
            response.setHeader("Hostname", environmentService.getServerName(request));
        }
        filterChain.doFilter(request,response);
    }
}
