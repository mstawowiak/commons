package com.github.mstawowiak.commons.rest.client.config;

import com.github.mstawowiak.commons.ssl.SSLConfiguration;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Webservice client settings
 */
public final class WebserviceClientSettings {

    public static final int DEFAULT_CONNECT_TIMEOUT = 3;
    public static final int DEFAULT_REQUEST_TIMEOUT = 5;

    /**
     * Webservice target URL
     */
    private final String url;

    /**
     * Amount of time to wait (in seconds) for the request to complete before giving up and timing out
     */
    private final int requestTimeout;

    /**
     * Amount of time to wait (in seconds) when initially establishing a connection before giving up and timing out
     */
    private final int connectTimeout;

    /**
     * Proxy URL
     */
    private final URL proxy;

    /**
     * Basic authentication credentials
     */
    private final BasicAuthentication basicAuthentication;

    /**
     * SSL configurator for SSL connections
     */
    private final SSLConfiguration sslConfiguration;

    /**
     * Additional properties for webservice client
     */
    private final Map<String, String> properties;

    /**
     * Additional components to register
     */
    private final List<Object> registeredComponents;

    private WebserviceClientSettings(Builder builder) {
        this.url = builder.url;
        this.requestTimeout = builder.requestTimeout;
        this.connectTimeout = builder.connectTimeout;
        this.proxy = builder.proxy;
        this.basicAuthentication = builder.basicAuthentication;
        this.sslConfiguration = builder.sslConfiguration;
        this.properties = builder.properties;
        this.registeredComponents = builder.registeredComponents;
    }

    public static class Builder {

        private final String url;
        private int requestTimeout = DEFAULT_REQUEST_TIMEOUT;
        private int connectTimeout = DEFAULT_CONNECT_TIMEOUT;
        private URL proxy;
        private BasicAuthentication basicAuthentication;
        private SSLConfiguration sslConfiguration;
        private final Map<String, String> properties = new HashMap<>();
        private final List<Object> registeredComponents = new ArrayList<>();

        public Builder(final String url) {
            Objects.requireNonNull(url, "Webservice 'url' cannot be null");
            this.url = url;
        }

        public boolean isHttpsUrl() {
            try {
                return "https".equalsIgnoreCase(new URL(url).getProtocol());
            } catch (MalformedURLException ex) { } //NOPMD ignore
            return false;
        }

        public Builder requestTimeout(int requestTimeout) {
            this.requestTimeout = requestTimeout;
            return this;
        }

        public Builder connectTimeout(int connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        public Builder proxy(URL proxy) {
            this.proxy = proxy;
            return this;
        }

        public Builder basicAuthentication(String username, String password) {
            this.basicAuthentication = new BasicAuthentication(username, password);
            return this;
        }

        public Builder sslConfiguration(SSLConfiguration sslConfiguration) {
            this.sslConfiguration = sslConfiguration;
            return this;
        }

        public Builder properties(Map<String, String> properties) {
            if (properties != null) {
                this.properties.putAll(properties);
            }

            return this;
        }

        public Builder property(String name, String value) {
            properties.put(name, value);
            return this;
        }

        public Builder registerComponent(final Object provider) {
            this.registeredComponents.add(provider);
            return this;
        }

        @SuppressWarnings("PMD.AccessorClassGeneration")
        public WebserviceClientSettings build() {
            return new WebserviceClientSettings(this);
        }
    }

    public String getUrl() {
        return url;
    }

    public int getRequestTimeout() {
        return requestTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public URL getProxy() {
        return proxy;
    }

    public BasicAuthentication getBasicAuthentication() {
        return basicAuthentication;
    }

    public SSLConfiguration getSslConfiguration() {
        return sslConfiguration;
    }

    public Map<String, String> getProperties() {
        return Collections.unmodifiableMap(properties);
    }

    public List<Object> getRegisteredComponents() {
        return Collections.unmodifiableList(registeredComponents);
    }

    @Override
    @SuppressWarnings("PMD.CyclomaticComplexity")
    public String toString() {
        StringBuilder sb = new StringBuilder(150);
        sb.append("WebserviceClientSettings [").append(System.lineSeparator())
                .append("url: ").append(url).append(System.lineSeparator())
                .append("requestTimeout: ").append(requestTimeout).append(System.lineSeparator())
                .append("connectTimeout: ").append(connectTimeout)
                .append("proxy: ").append(proxy == null ? "no proxy" : proxy).append(System.lineSeparator());
        if (basicAuthentication != null) {
            sb.append(System.lineSeparator())
                    .append(basicAuthentication.toString());
        }
        if (sslConfiguration != null) {
            sb.append(System.lineSeparator())
                    .append(sslConfiguration.toString());
        }
        if (properties != null && !properties.isEmpty()) {
            sb.append(System.lineSeparator())
                    .append("properties: ").append(Arrays.toString(properties.entrySet().toArray()));
        }
        if (registeredComponents != null && !registeredComponents.isEmpty()) {
            sb.append(System.lineSeparator())
                    .append("registeredComponents: ")
                    .append(registeredComponents.stream()
                            .map(component -> {
                                if (component instanceof Class<?>) {
                                    return ((Class<?>) component).getName();
                                } else {
                                    return component.getClass().getName();
                                }
                            })
                            .collect(Collectors.joining(",")));
        }
        sb.append(']');

        return sb.toString();
    }
}