package com.github.mstawowiak.commons.logging.logback.property;

import ch.qos.logback.classic.Level;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for classes which extends {@link AbstractPropertyDefiner}
 */
public class LoggingPropertyDefinerTest {

    @BeforeClass
    public static void setUpClass() {
        System.setProperty(LoggingPathProperty.LOGGING_PATH_KEY, "/app/logs");
        System.setProperty(AppLevelProperty.APP_LEVEL_KEY, Level.DEBUG.levelStr);
        System.setProperty(WebserviceLevelProperty.WEBSERVICE_LEVEL_KEY, Level.INFO.levelStr);
    }

    @AfterClass
    public static void tearDownClass() {
        System.clearProperty(LoggingPathProperty.LOGGING_PATH_KEY);
        System.clearProperty(AppLevelProperty.APP_LEVEL_KEY);
        System.clearProperty(WebserviceLevelProperty.WEBSERVICE_LEVEL_KEY);
    }

    @Test
    public void shouldLoadCorrectLoggingPath() {
        LoggingPathProperty property = new LoggingPathProperty();

        Assert.assertEquals("/app/logs", property.getPropertyValue());
    }

    @Test
    public void shouldLoadCorrectAppLevel() {
        AppLevelProperty property = new AppLevelProperty();

        Assert.assertEquals("DEBUG", property.getPropertyValue());
    }

    @Test
    public void shouldLoadCorrectWebserviceLevel() {
        WebserviceLevelProperty property = new WebserviceLevelProperty();

        Assert.assertEquals("INFO", property.getPropertyValue());
    }

}
