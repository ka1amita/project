package com.gfa.configs;

import static org.junit.jupiter.api.Assertions.*;

import com.gfa.config.PaginationProperties;
import javax.validation.Validation;
import javax.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class) // integrates Spring’s TestContext framework with JUnit5
@EnableConfigurationProperties(PaginationProperties.class) // enables support for @ConfigurationProperties beans
@TestPropertySource(value = "classpath:positive-test.properties") // specifies a testing file that overrides the default application.properties
class PositivePaginationPropertiesTests {

    @Autowired // works despite IntelliJ error highlight (doesn't work without the annotation!
    PaginationProperties paginationProperties;
    private static Validator validator;

    @BeforeAll
    public static void setup() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void reads_valid_properties() {
        assertEquals(0, validator.validate(paginationProperties.getPageSizeDefault()).size());
        assertEquals(0, validator.validate(paginationProperties.getPageSizeMax()).size());
        assertEquals(0, validator.validate(paginationProperties.getSortBy()).size());
        assertEquals(0, validator.validate(paginationProperties.getSortOrder()).size());
        assertEquals(5, paginationProperties.getPageSizeDefault());
        assertEquals(10, paginationProperties.getPageSizeMax());
        assertEquals("username", paginationProperties.getSortBy());
        assertEquals(Sort.Direction.DESC, paginationProperties.getSortOrder());
    }
}
