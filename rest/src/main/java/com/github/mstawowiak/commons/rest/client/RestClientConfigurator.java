package com.github.mstawowiak.commons.rest.client;

import com.github.mstawowiak.commons.logging.SimpleLoggerFactory;
import com.github.mstawowiak.commons.rest.Service;
import com.github.mstawowiak.commons.rest.client.config.WebserviceClientSettings;
import com.github.mstawowiak.commons.rest.exception.WebserviceClientConfigurationException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;

public final class RestClientConfigurator {

    private static final Logger LOGGER = SimpleLoggerFactory.getLogger();

    private final Map<Service, List<WebserviceClientSettings>> settingsMap;

    private static final RestClientConfigurator INSTANCE = new RestClientConfigurator();

    private RestClientConfigurator() {
        settingsMap = new ConcurrentHashMap<>();
    }

    public static void addSettings(final Service service, final WebserviceClientSettings settings) {
        Objects.requireNonNull(settings, "WebserviceClientSettings cannot be null");

        getInstance().putIntoMap(service, Collections.singletonList(settings));
    }

    public static RestClientConfigurator getInstance() {
        return INSTANCE;
    }

    public List<WebserviceClientSettings> getSettings(final Service service) {
        List<WebserviceClientSettings> result = settingsMap.get(service);
        if (result == null) {
            throw new WebserviceClientConfigurationException(
                    "Rest service '" + service.getName() + "' is not configured.");
        }
        return result;
    }

    public Map<Service, List<WebserviceClientSettings>> getAllSettings() {
        return Collections.unmodifiableMap(settingsMap);
    }

    private synchronized void putIntoMap(Service service, List<WebserviceClientSettings> settings) {
        if (settings == null || settings.isEmpty()) {
            throw new WebserviceClientConfigurationException(
                    "WebserviceClientSettings list for service must be not empty.");
        }
        if (settingsMap.get(service) == null) {
            settingsMap.put(service, Collections.unmodifiableList(settings));
            LOGGER.info("Rest service '{}' configuration cached. {}", service.getName(), settings.toString());
        } else {
            LOGGER.info("Rest service '{}' already configured.", service.getName());
        }
    }
}