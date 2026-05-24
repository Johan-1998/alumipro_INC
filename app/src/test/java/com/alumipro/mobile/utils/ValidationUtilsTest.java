package com.alumipro.mobile.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ValidationUtilsTest {

    @Test
    public void shouldValidateEmail() {
        assertTrue(ValidationUtils.isEmail("admin@alumipro.com"));
        assertFalse(ValidationUtils.isEmail("admin-alumipro.com"));
    }

    @Test
    public void shouldNormalizeBaseUrl() {
        assertEquals("http://192.168.0.5:8080/", ValidationUtils.normalizeBaseUrl("192.168.0.5:8080"));
    }
}
