package com.github.mstawowiak.commons.logging.logback.property;

import ch.qos.logback.classic.Level;

abstract class AbstractLevelPropertyDefiner extends AbstractPropertyDefiner<Level> {

    public AbstractLevelPropertyDefiner() {
        defaultValue = defaultValue().levelStr;
    }

    @Override
    public void setDefault(String defaultFromLogbackXml) {
        defaultValue = Level.toLevel(defaultFromLogbackXml, defaultValue()).levelStr;
    }
}
