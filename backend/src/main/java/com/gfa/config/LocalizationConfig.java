package com.gfa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
@Configuration
public class LocalizationConfig implements WebMvcConfigurer {

    @Bean
    public LocaleResolver localeResolver(){
        return new SmartLocaleResolver();
    }

    public static class SmartLocaleResolver implements LocaleResolver{
        private final AcceptHeaderLocaleResolver apiResolver = new AcceptHeaderLocaleResolver();
        private final SessionLocaleResolver webResolver = new SessionLocaleResolver();
        @Override
        public Locale resolveLocale(HttpServletRequest request) {
            String langParam = request.getParameter("lang");
            if (langParam != null && !langParam.isEmpty()) {
                return webResolver.resolveLocale(request);
            } else {
                return apiResolver.resolveLocale(request);
            }
        }
        @Override
        public void setLocale (HttpServletRequest request, HttpServletResponse response, Locale locale){
            webResolver.setLocale(request,response,locale);
        }
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor(){
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(localeChangeInterceptor());
    }
}
