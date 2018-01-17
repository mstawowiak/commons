package com.github.mstawowiak.commons.cdi;

import java.lang.annotation.Annotation;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.util.TypeLiteral;

/**
 * Simplifies selection of CDI beans from the current container.
 */
public final class CDIContainer {

    public static <T> T select(Class<T> subtype, Annotation... qualifiers) {
        return CDI.current().select(subtype, qualifiers).get();
    }

    public static <T> T select(TypeLiteral<T> subtype, Annotation... qualifiers) {
        return CDI.current().select(subtype, qualifiers).get();
    }

    public static BeanManager getBeanManager() {
        return CDI.current().getBeanManager();
    }

    private CDIContainer() {
    }

}
