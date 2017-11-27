package com.github.mstawowiak.commons.logging.logback.property;

abstract class AbstractStringPropertyDefiner extends AbstractPropertyDefiner<String> {

    public AbstractStringPropertyDefiner() {
        defaultValue = defaultValue();
    }

    @Override
    public void setDefault(String defaultFromLogbackXml) {
        if (defaultFromLogbackXml != null && !defaultFromLogbackXml.isEmpty()) {
            defaultValue = defaultFromLogbackXml;
        }
    }
}
