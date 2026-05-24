package com.alumipro.mobile.utils;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class FormatUtilsTest {

    @Test
    public void shouldFormatCurrency() {
        assertTrue(FormatUtils.currency(150000).contains("150"));
    }
}
