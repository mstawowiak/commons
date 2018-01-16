package com.github.mstawowiak.commons.rest.logging;

import org.slf4j.bridge.SLF4JBridgeHandler;

public final class WebserviceLoggerUtils {

    public static void registerSLF4JBridgeHandler() {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    private WebserviceLoggerUtils() {
    }
}
