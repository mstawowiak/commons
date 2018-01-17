package com.github.mstawowiak.commons.logging.logback.property;

import ch.qos.logback.classic.Level;

/**
 * Property for set logging level of webservice (REST/SOAP) calls
 */
public class WebserviceLevelProperty extends AbstractLevelPropertyDefiner {

    static final String WEBSERVICE_LEVEL_KEY = "com.github.mstawowiak.commons.logging.webservice.level";

    @Override
    public String propertyKey() {
        return WEBSERVICE_LEVEL_KEY;
    }

    @Override
    public Level defaultValue() {
        return Level.OFF;
    }
}
