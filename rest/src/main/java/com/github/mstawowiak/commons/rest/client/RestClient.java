package com.github.mstawowiak.commons.rest.client;

import com.github.mstawowiak.commons.logging.SimpleLoggerFactory;
import com.github.mstawowiak.commons.rest.Service;
import com.github.mstawowiak.commons.rest.client.config.WebserviceClientSettings;
import com.github.mstawowiak.commons.rest.client.proxy.ClientProxySelector;
import com.github.mstawowiak.commons.rest.exception.WebserviceException;
import com.github.mstawowiak.commons.rest.exception.WebserviceUnavailableException;
import com.github.mstawowiak.commons.rest.logging.LoggingFeatureFactory;
import com.github.mstawowiak.commons.rest.logging.WebserviceLoggerUtils;
import com.github.mstawowiak.commons.ssl.NaiveHostnameVerifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.client.filter.EncodingFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.message.GZipEncoder;
import org.slf4j.Logger;

@SuppressWarnings({"PMD.GodClass", "PMD.ExcessiveImports"})
public final class RestClient {

    private static final Logger LOGGER = SimpleLoggerFactory.getLogger();

    private static final String REST_CLIENT_LOGGER_NAME = "rest_client_messages";
    private static final String WADL_PATH = "/application.wadl";

    /**
     * WebTarget objects are cached due to high creation cost
     */
    private static final Map<Service, List<WebTarget>> WEB_TARGET_CACHE = new ConcurrentHashMap<>();

    /**
     * Client objects are cached due to high creation cost
     */
    private static final Map<Service, List<Client>> CLIENT_CACHE = new ConcurrentHashMap<>();

    /**
     * Default client creating dynamic targets
     */
    private static final Client DEFAULT_CLIENT = createClient(
            new WebserviceClientSettings.Builder("fakeUrl").build());

    private RestClient() {
    }

    /**
     * Returns {@link WebTarget} for given service. Service settings must be defined.
     * @param service service for which {@link WebTarget} will be returned
     * @return cached {@link WebTarget} instance
     */
    public static WebTarget getTarget(final Service service) {
        List<WebTarget> webTargets = getTargets(service);
        if (webTargets.size() > NumberUtils.INTEGER_ONE) {
            LOGGER.warn("Rest service '{}' has defined more than one target. "
                    + "Probably you should use 'RestClient.getTargets(service)' method.", service.getName());
        }

        return webTargets.get(0);
    }

    /**
     * Returns {@link WebTarget} with default client settings
     *
     * @param url address of webservice
     * @return {@link WebTarget} for default client settings
     */
    public static WebTarget getTarget(String url) {
        Objects.requireNonNull("Webservice 'url' cannot be null.");

        return DEFAULT_CLIENT.target(url);
    }

    /**
     * Returns list of {@link WebTarget} for given service.
     *
     * @param service service for which {@link WebTarget} instances will be returned
     * @return cached list of {@link WebTarget} instances
     */
    public static List<WebTarget> getTargets(Service service) {
        List<WebTarget> webTargets = WEB_TARGET_CACHE.get(service);
        if (webTargets == null) {
            List<WebserviceClientSettings> settingsList = RestClientConfigurator.getInstance().getSettings(service);

            webTargets = new ArrayList<>();
            for (WebserviceClientSettings settings : settingsList) {
                webTargets.add(createWebTarget(settings));
            }
            WEB_TARGET_CACHE.put(service, Collections.unmodifiableList(webTargets));
        }

        return webTargets;
    }

    /**
     * Returns {@link Client} for given service
     * @param service service for which {@link Client} instance will be returned
     * @return cached {@link Client} instance
     */
    public static Client getClient(Service service) {
        Client client;
        List<Client> clients = CLIENT_CACHE.get(service);
        if (clients == null) {
            List<WebserviceClientSettings> settings = RestClientConfigurator.getInstance().getSettings(service);

            client = createClient(settings.get(0));
            CLIENT_CACHE.computeIfAbsent(service, key -> new ArrayList<>()).add(client);
        } else {
            client = clients.get(0);
        }
        return client;
    }

    private static WebTarget createWebTarget(WebserviceClientSettings settings) {
        if (settings.getProxy() != null) {
            ClientProxySelector.registerProxyMapping(settings.getUrl(), settings.getProxy());
        }

        return createClient(settings).target(settings.getUrl());
    }

    @SuppressWarnings({"PMD.CyclomaticComplexity", "PMD.NPathComplexity"})
    private static Client createClient(WebserviceClientSettings settings) {
        ClientConfig clientConfig = new ClientConfig();

        //set up logging
        setLogging(clientConfig);

        //set up timeouts
        setTimeouts(clientConfig, settings.getConnectTimeout(), settings.getRequestTimeout());

        //custom providers
        settings.getRegisteredComponents().forEach(provider -> {
            if (provider instanceof Class<?>) {
                clientConfig.register((Class<?>)provider);
            } else {
                clientConfig.register(provider);
            }
        });

        //register GZip encoder
        if (!clientConfig.isRegistered(EncodingFilter.class)) {
            clientConfig.register(EncodingFilter.class);
        }
        if (!clientConfig.isRegistered(GZipEncoder.class)) {
            clientConfig.register(GZipEncoder.class);
        }

        //json provider
        if (!clientConfig.isRegistered(JacksonJaxbJsonProvider.class)) {
            clientConfig.register(JacksonJaxbJsonProvider.class);
        }
        if (!clientConfig.isRegistered(JacksonFeature.class)) {
            clientConfig.register(JacksonFeature.class);
        }

        //set up Basic Auth
        if (settings.getBasicAuthentication() != null) {
            HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(
                    settings.getBasicAuthentication().getUsername(),
                    String.copyValueOf(settings.getBasicAuthentication().getPassword()));
            clientConfig.register(feature);
        }

        if (settings.getProperties() != null) {
            settings.getProperties().forEach(clientConfig::property);
        }

        ClientBuilder clientBuilder = ClientBuilder.newBuilder()
                .withConfig(clientConfig);

        if (settings.getSslConfiguration() != null) {
            clientBuilder = clientBuilder.sslContext(
                    settings.getSslConfiguration().createSSLContext());

            if (!settings.getSslConfiguration().isVerifyHostname()) {
                clientBuilder = clientBuilder.hostnameVerifier(
                        new NaiveHostnameVerifier());
            }
        }

        return clientBuilder.build();
    }

    private static void setTimeouts(ClientConfig clientConfig, int connectTimeout, int requestTimeout) {
        clientConfig.property(ClientProperties.CONNECT_TIMEOUT, connectTimeout * 1000);
        clientConfig.property(ClientProperties.READ_TIMEOUT, requestTimeout * 1000);
    }

    private static void setLogging(ClientConfig clientConfig) {
        clientConfig.register(LoggingFeatureFactory.create(REST_CLIENT_LOGGER_NAME));

        WebserviceLoggerUtils.registerSLF4JBridgeHandler();
    }

    /**
     * Method proccessing web service response. This method can:
     * - return T class object
     * - throw WebserviceException with error details
     * - throw ClientErrorException for HTTP 4XX error codes
     * - throw WebApplicationException in case of failure
     *
     * @param <T> class parameter to which result should be cast
     * @param response REST webs ervice respone
     * @param resultClazz class parameter to which result should be cast
     * @return response as a given class
     * @throws WebserviceException
     * @throws ClientErrorException
     */
    public static <T> T getResponse(Response response, Class<T> resultClazz)
            throws WebserviceException, ClientErrorException {

        T result = null;

        if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
            if (resultClazz == null) {
                try {
                    response.close();
                } catch (ProcessingException e) { //NOPMD - EmptyCatchBlock - ignore and return this same
                    //do nothing
                }
            } else {
                try {
                    result = response.readEntity(resultClazz);
                } catch (ProcessingException e) {
                    throw new WebApplicationException("Failed to cast response to class: " + resultClazz.getName(), e);
                }
            }
        } else {
            processInvalidResponse(response);
        }

        return result;
    }

    /**
     * Method proccessing web service response. This method can:
     * - return T class object
     * - throw WebserviceException with error details
     * - throw ClientErrorException for HTTP 4XX error codes
     * - throw WebApplicationException in case of failure
     *
     * @param <T> class parameter to which result should be cast
     * @param response REST webservice respone
     * @param resultClazz class parameter to which result should be cast
     * @return response as a given class
     * @throws WebserviceException
     * @throws ClientErrorException
     */
    public static <T> T getResponse(Response response, GenericType<T> resultClazz)
            throws WebserviceException, ClientErrorException {

        T result = null;

        if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
            if (resultClazz == null) {
                try {
                    response.close();
                } catch (ProcessingException e) { //NOPMD - EmptyCatchBlock - ignore and return this same
                    //do nothing
                }
            } else {
                try {
                    result = response.readEntity(resultClazz);
                } catch (ProcessingException e) {
                    throw new WebApplicationException("Failed to cast response to class: "
                            + resultClazz.getType().toString(), e);
                }
            }
        } else {
            processInvalidResponse(response);
        }

        return result;
    }

    /**
     * Check if {@link Response} returned successful response
     *
     * @param response REST webservice respone
     * @throws WebserviceException
     * @throws ClientErrorException
     */
    public static void checkResponse(Response response) throws WebserviceException, ClientErrorException {
        if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
            processInvalidResponse(response);
        }
    }

    private static void processInvalidResponse(Response response) {
        if (response.getStatus() == WebserviceException.HTTP_STATUS_CODE) {
            processWebserviceException(response);
        } else if (response.getStatusInfo().getFamily() == Response.Status.Family.CLIENT_ERROR) {
            processClientError(response);
        } else {
            processOtherError(response);
        }
    }

    private static void processWebserviceException(Response response) {
        try {
            throw response.readEntity(WebserviceException.class);
        } catch (ProcessingException e) {
            throw new WebApplicationException("Failed to cast response to WebserviceException.class", e);
        }
    }

    private static void processClientError(Response response) {
        try {
            String customClientErrorMsq = response.readEntity(String.class);
            if (!StringUtils.isEmpty(customClientErrorMsq)) {
                String errorMsgPrefix = buildErrorMessagePrefix(response);
                if (StringUtils.equals(customClientErrorMsq, errorMsgPrefix)) {
                    throw new ClientErrorException(customClientErrorMsq,
                            response.getStatus());
                } else {
                    throw new ClientErrorException(errorMsgPrefix + ": " + customClientErrorMsq,
                            response.getStatus());
                }
            }
        } catch (ProcessingException e) { //NOPMD - EmptyCatchBlock - ignore and throw this same
            //do nothing
        }
        throw new ClientErrorException(response);
    }

    private static void processOtherError(Response response) {
        StringBuilder msg = new StringBuilder()
                .append("Failed to get response. ")
                .append(buildErrorMessagePrefix(response));
        try {
            String msgFromException = response.readEntity(String.class);
            if (StringUtils.isNotEmpty(msgFromException)) {
                msg.append(": ");
                msg.append(msgFromException);
            }
        } catch (ProcessingException e) { //NOPMD - EmptyCatchBlock - ignore and throw this same
            //do nothing
        }
        throw new WebApplicationException(msg.toString());
    }

    private static String buildErrorMessagePrefix(Response response) {
        return new StringBuilder(20)
                .append("HTTP ")
                .append(response.getStatusInfo().getStatusCode()).append(' ')
                .append(response.getStatusInfo().getReasonPhrase()).toString();
    }

    /**
     * Check if given service is available by making request to default WADL localization.
     *
     * @param service service for which health status should be checked
     */
    public static void healthCheck(Service service) {
        healthCheck(service, WADL_PATH);
    }

    /**
     * Check if given service is available by making request to given path.
     *
     * @param service service for which health status should be checked
     * @param path path for checking health status of given service
     */
    public static void healthCheck(Service service, String path) {
        List<WebTarget> webTargets = RestClient.getTargets(service);

        Exception lastError = null;
        for (WebTarget webTarget : webTargets) {
            try {
                Response response = webTarget
                        .path(path)
                        .request()
                        .get();
                RestClient.checkResponse(response);
                return;
            } catch (Exception ex) {
                lastError = ex;
            }
        }
        if (lastError != null) {
            throw new WebserviceUnavailableException("Service '" + service.getName()
                    + "' is not available: ", lastError);
        }
    }
}