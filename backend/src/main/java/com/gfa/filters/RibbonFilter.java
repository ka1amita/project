package com.gfa.filters;

import com.gfa.config.RibbonProperties;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class RibbonFilter extends OncePerRequestFilter {

    private final RibbonProperties ribbonProperties;

    @Autowired
    public RibbonFilter(RibbonProperties ribbonProperties) {
        this.ribbonProperties = ribbonProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (ribbonProperties.isEnabled()) {
            response.setHeader("Environment", System.getenv("ENV"));
            response.setHeader("Hostname", request.getServerName());
        }

        filterChain.doFilter(request,response);
    }
}
