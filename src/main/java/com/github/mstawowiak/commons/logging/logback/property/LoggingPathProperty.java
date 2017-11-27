package com.github.mstawowiak.commons.logging.logback.property;

/**
 * Property for set logging path
 */
public class LoggingPathProperty extends AbstractStringPropertyDefiner {

    static final String LOGGING_PATH_KEY = "com.github.mstawowiak.commons.logging.path";

    private static final String LOGGING_PATH_DEFAULT = "logs";

    @Override
    public String propertyKey() {
        return LOGGING_PATH_KEY;
    }

    @Override
    public String defaultValue() {
        return LOGGING_PATH_DEFAULT;
    }
}
