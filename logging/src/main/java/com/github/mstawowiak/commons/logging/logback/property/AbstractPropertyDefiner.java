package com.github.mstawowiak.commons.logging.logback.property;

import ch.qos.logback.core.PropertyDefinerBase;

abstract class AbstractPropertyDefiner<T> extends PropertyDefinerBase {

    protected String defaultValue;

    public abstract String propertyKey();

    public abstract T defaultValue();

    public abstract void setDefault(String defaultFromLogbackXml);

    @Override
    public String getPropertyValue() {
        return System.getProperty(propertyKey(), defaultValue);
    }
}
