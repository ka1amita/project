package com.matejkala.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/strings")
public class StringsController {

    @Autowired
    private ReloadableResourceBundleMessageSource messageSource;

    @GetMapping
    public ResponseEntity<Map<String, Map<String, String>>> strings() throws IOException {
        Map<String, Map<String, String>> response = new HashMap<>();

        List<String> allKeys = getAllKeysFromProperties();

        List<Locale> locales = Arrays.asList(Locale.ENGLISH, new Locale("cz"), new Locale("hu"));

        for (String key : allKeys) {//for each key from message.properties
            System.err.println(key);
            for (Locale locale : locales) {
                System.err.println(locale);
                String value = messageSource.getMessage(key, null, locale);
                System.err.println(value);
                response.computeIfAbsent(key, k -> new HashMap<>()).put(locale.toString(), value);
            }
        }
        return ResponseEntity.ok(response);
    }

    private List<String> getAllKeysFromProperties() throws IOException {
        List<String> keys = new ArrayList<>();

            Resource resource = new PathMatchingResourcePatternResolver().getResource("classpath:messages.properties");
            Properties properties = new Properties();
            properties.load(resource.getInputStream());

            for (Object key : properties.keySet()) {
                keys.add(key.toString());
            }
        return keys;
    }
}
