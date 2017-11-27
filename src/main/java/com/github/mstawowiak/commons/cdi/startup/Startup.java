package com.github.mstawowiak.commons.cdi.startup;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Stereotype;
import javax.enterprise.util.Nonbinding;

/**
 * Marks beans for startup when the application context is initialized.
 */
@ApplicationScoped
@Documented
@Stereotype
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Startup {

    /**
     * Prioritizes this startup.
     * Lower value indicates faster startup.
     */
    @Nonbinding
    int priority() default 100;

    /**
     * Indicates blocking application startup if bean annotated by {@link Startup} cannot be initialized.
     */
    @Nonbinding
    boolean blocking() default true;
}
