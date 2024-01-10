package com.matejkala.configs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.matejkala.config.PaginationProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(PaginationProperties.class)
// @TestPropertySource("classpath:negative-test.properties") // not specified so the hard-coded default values are used
public class NegativePaginationPropertiesTests {

    @Autowired
    PaginationProperties paginationProperties;

    @Test
    void sets_default_properties_when_properties_not_set() {
        assertEquals(20, paginationProperties.getPageSizeDefault());
        assertEquals(100, paginationProperties.getPageSizeMax());
        assertEquals("id", paginationProperties.getSortBy());
        assertEquals(Sort.Direction.ASC, paginationProperties.getSortOrder());
    }
}
