package com.github.mstawowiak.commons.logging.logback.property;

import ch.qos.logback.classic.Level;

/**
 * Property for set logging level of application
 */
public class AppLevelProperty extends AbstractLevelPropertyDefiner {

    static final String APP_LEVEL_KEY = "com.github.mstawowiak.commons.logging.app.level";

    @Override
    public String propertyKey() {
        return APP_LEVEL_KEY;
    }

    @Override
    public Level defaultValue() {
        return Level.INFO;
    }
}
