package com.github.mstawowiak.commons.rest.logging;

import org.glassfish.jersey.logging.LoggingFeature;

public final class LoggingFeatureFactory {

    public static LoggingFeature create(String loggerName) {
        return new LoggingFeature(
                java.util.logging.Logger.getLogger(loggerName),
                java.util.logging.Level.INFO, null, null);
    }

    private LoggingFeatureFactory() {
    }
}