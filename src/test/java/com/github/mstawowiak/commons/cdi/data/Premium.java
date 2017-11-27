package com.github.mstawowiak.commons.cdi.data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Qualifier;

@Qualifier
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Premium {

    final class Literal extends AnnotationLiteral<Premium> implements Premium {
        public static final Literal INSTANCE = new Literal();

        private static final long serialVersionUID = 1L;
    }

}
