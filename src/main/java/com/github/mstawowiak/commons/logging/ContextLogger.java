package com.github.mstawowiak.commons.logging;

import org.slf4j.Logger;
import org.slf4j.Marker;

/**
 * Logger for log messages with context on the beginning of message content.
 */
@SuppressWarnings("PMD.GodClass")
public class ContextLogger {

    private final Logger logger;

    public ContextLogger(Logger logger) {
        this.logger = logger;
    }

    public String getName() {
        return logger.getName();
    }

    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    public boolean isTraceEnabled(Marker marker) {
        return logger.isInfoEnabled(marker);
    }

    public void trace(String context, String string) {
        logger.trace(concatMessage(context, string));
    }

    public void trace(String context, String string, Object obj) {
        logger.trace(concatMessage(context, string), obj);
    }

    public void trace(String context, String string, Object obj, Object obj1) {
        logger.trace(concatMessage(context, string), obj, obj1);
    }

    public void trace(String context, String string, Object... os) {
        logger.trace(concatMessage(context, string), os);
    }

    public void trace(String context, String string, Throwable thrwbl) {
        logger.trace(concatMessage(context, string), thrwbl);
    }

    public void trace(String context, Marker marker, String string) {
        logger.trace(marker, concatMessage(context, string));
    }

    public void trace(String context, Marker marker, String string, Object obj) {
        logger.trace(marker, concatMessage(context, string), obj);
    }

    public void trace(String context, Marker marker, String string, Object obj, Object obj1) {
        logger.trace(marker, concatMessage(context, string), obj, obj1);
    }

    public void trace(String context, Marker marker, String string, Object... os) {
        logger.trace(marker, concatMessage(context, string), os);
    }

    public void trace(String context, Marker marker, String string, Throwable thrwbl) {
        logger.trace(marker, concatMessage(context, string), thrwbl);
    }

    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    public boolean isDebugEnabled(Marker marker) {
        return logger.isDebugEnabled(marker);
    }

    public void debug(String context, String string) {
        logger.debug(concatMessage(context, string));
    }

    public void debug(String context, String string, Object obj) {
        logger.debug(concatMessage(context, string), obj);
    }

    public void debug(String context, String string, Object obj, Object obj1) {
        logger.debug(concatMessage(context, string), obj, obj1);
    }

    public void debug(String context, String string, Object... os) {
        logger.debug(concatMessage(context, string), os);
    }

    public void debug(String context, String string, Throwable thrwbl) {
        logger.debug(concatMessage(context, string), thrwbl);
    }

    public void debug(String context, Marker marker, String string) {
        logger.debug(marker, concatMessage(context, string));
    }

    public void debug(String context, Marker marker, String string, Object obj) {
        logger.debug(marker, concatMessage(context, string), obj);
    }

    public void debug(String context, Marker marker, String string, Object obj, Object obj1) {
        logger.debug(marker, concatMessage(context, string), obj, obj1);
    }

    public void debug(String context, Marker marker, String string, Object... os) {
        logger.debug(marker, concatMessage(context, string), os);
    }

    public void debug(String context, Marker marker, String string, Throwable thrwbl) {
        logger.debug(marker, concatMessage(context, string), thrwbl);
    }

    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    public boolean isInfoEnabled(Marker marker) {
        return logger.isInfoEnabled(marker);
    }

    public void info(String context, String string) {
        logger.info(concatMessage(context, string));
    }

    public void info(String context, String string, Object obj) {
        logger.info(concatMessage(context, string), obj);
    }

    public void info(String context, String string, Object obj, Object obj1) {
        logger.info(concatMessage(context, string), obj, obj1);
    }

    public void info(String context, String string, Object... os) {
        logger.info(concatMessage(context, string), os);
    }

    public void info(String context, String string, Throwable thrwbl) {
        logger.info(concatMessage(context, string), thrwbl);
    }

    public void info(String context, Marker marker, String string) {
        logger.info(marker, concatMessage(context, string));
    }

    public void info(String context, Marker marker, String string, Object obj) {
        logger.info(marker, concatMessage(context, string), obj);
    }

    public void info(String context, Marker marker, String string, Object obj, Object obj1) {
        logger.info(marker, concatMessage(context, string), obj, obj1);
    }

    public void info(String context, Marker marker, String string, Object... os) {
        logger.info(marker, concatMessage(context, string), os);
    }

    public void info(String context, Marker marker, String string, Throwable thrwbl) {
        logger.info(marker, concatMessage(context, string), thrwbl);
    }

    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    public boolean isWarnEnabled(Marker marker) {
        return logger.isWarnEnabled(marker);
    }

    public void warn(String context, String string) {
        logger.warn(concatMessage(context, string));
    }

    public void warn(String context, String string, Object obj) {
        logger.warn(concatMessage(context, string), obj);
    }

    public void warn(String context, String string, Object obj, Object obj1) {
        logger.warn(concatMessage(context, string), obj, obj1);
    }

    public void warn(String context, String string, Object... os) {
        logger.warn(concatMessage(context, string), os);
    }

    public void warn(String context, String string, Throwable thrwbl) {
        logger.warn(concatMessage(context, string), thrwbl);
    }

    public void warn(String context, Marker marker, String string) {
        logger.warn(marker, concatMessage(context, string));
    }

    public void warn(String context, Marker marker, String string, Object obj) {
        logger.warn(marker, concatMessage(context, string), obj);
    }

    public void warn(String context, Marker marker, String string, Object obj, Object obj1) {
        logger.warn(marker, concatMessage(context, string), obj, obj1);
    }

    public void warn(String context, Marker marker, String string, Object... os) {
        logger.warn(marker, concatMessage(context, string), os);
    }

    public void warn(String context, Marker marker, String string, Throwable thrwbl) {
        logger.warn(marker, concatMessage(context, string), thrwbl);
    }

    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    public boolean isErrorEnabled(Marker marker) {
        return logger.isErrorEnabled(marker);
    }

    public void error(String context, String string) {
        logger.error(concatMessage(context, string));
    }

    public void error(String context, String string, Object obj) {
        logger.error(concatMessage(context, string), obj);
    }

    public void error(String context, String string, Object obj, Object obj1) {
        logger.error(concatMessage(context, string), obj, obj1);
    }

    public void error(String context, String string, Object... os) {
        logger.error(concatMessage(context, string), os);
    }

    public void error(String context, String string, Throwable thrwbl) {
        logger.error(concatMessage(context, string), thrwbl);
    }

    public void error(String context, Marker marker, String string) {
        logger.error(marker, concatMessage(context, string));
    }

    public void error(String context, Marker marker, String string, Object obj) {
        logger.error(marker, concatMessage(context, string), obj);
    }

    public void error(String context, Marker marker, String string, Object obj, Object obj1) {
        logger.error(marker, concatMessage(context, string), obj, obj1);
    }

    public void error(String context, Marker marker, String string, Object... os) {
        logger.error(marker, concatMessage(context, string), os);
    }

    public void error(String context, Marker marker, String string, Throwable thrwbl) {
        logger.error(marker, concatMessage(context, string), thrwbl);
    }

    /**
     * Add context to log message.
     *
     * @param context context of message
     * @param message body of message
     * @return log message with context
     */
    private static String concatMessage(String context, String message) {
        StringBuilder sb = new StringBuilder();
        if (context != null) {
            sb.append('[').append(context).append("] ");
        }
        sb.append(message);

        return sb.toString();
    }
}
