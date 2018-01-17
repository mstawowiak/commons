package com.github.mstawowiak.commons.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory which returns SLF4J {@link Logger} for the class from which the call was made.
 */
public final class SimpleLoggerFactory {

    public static Logger getLogger() {
        Throwable throwable = new Throwable();
        StackTraceElement caller = throwable.getStackTrace()[1];

        return LoggerFactory.getLogger(caller.getClassName());
    }

    private SimpleLoggerFactory() {
    }

}
