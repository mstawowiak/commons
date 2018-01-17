package com.github.mstawowiak.commons.logging;

import org.slf4j.LoggerFactory;

/**
 * Factory for {@link ContextLogger}
 */
public final class ContextLoggerFactory {

    public static ContextLogger getLogger() {
        Throwable throwable = new Throwable();
        StackTraceElement caller = throwable.getStackTrace()[1];

        return new ContextLogger(LoggerFactory.getLogger(caller.getClassName()));
    }

    private ContextLoggerFactory() {
    }
}
