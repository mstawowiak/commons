package com.github.mstawowiak.commons.logging.logback.property;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for classes which extends {@link AbstractPropertyDefiner} with default values
 */
public class LoggingPropertyDefinerDefaultTest {

    @Test
    public void shouldLoadCorrectDefaultLoggingPath() {
        LoggingPathProperty property = new LoggingPathProperty();

        Assert.assertEquals("logs", property.getPropertyValue());
    }

    @Test
    public void shouldLoadCorrectDefaultAppLevel() {
        AppLevelProperty property = new AppLevelProperty();

        Assert.assertEquals("INFO", property.getPropertyValue());
    }

    @Test
    public void shouldLoadCorrectDefaultWebserviceLevel() {
        WebserviceLevelProperty property = new WebserviceLevelProperty();

        Assert.assertEquals("OFF", property.getPropertyValue());
    }
}
