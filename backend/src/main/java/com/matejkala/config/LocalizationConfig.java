package com.matejkala.config;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
@Configuration
public class LocalizationConfig implements WebMvcConfigurer {

    @Bean
    public LocaleResolver localeResolver(){
        // interestingly enough this default (if e.g. set to "cz") is overridden with english if
        // the "Accept-Language" header is empty, but not if it is missing nor if it is set
        // to something silly; the message.properties files change nothing about it.
        Locale defaultLocale = Locale.ENGLISH;
        // if the language is missing, then it really doesn't localize it into; only english is supported despite that
        List<Locale> supportedLocales = Arrays.asList(new Locale("cz"), new Locale("hu"));

        AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
        localeResolver.setDefaultLocale(defaultLocale);
        localeResolver.setSupportedLocales(supportedLocales);
        return localeResolver;
    }
}

