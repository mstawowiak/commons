package com.github.mstawowiak.commons.rest.client.proxy;

import com.github.mstawowiak.commons.logging.SimpleLoggerFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;

public final class ClientProxySelector extends ProxySelector {

    private static final Logger LOGGER = SimpleLoggerFactory.getLogger();

    private static final ProxySelector DEFAULT;

    private static final List<Address> ADDRESSES;

    static {
        ADDRESSES = new ArrayList<>();

        DEFAULT = ProxySelector.getDefault();

        ProxySelector.setDefault(new ClientProxySelector());
        LOGGER.info("ClientProxySelector class initialized.");
    }

    private ClientProxySelector() {
        LOGGER.info("ClientProxySelector created.");
    }

    /**
     * Registers proxy for target URI
     *
     * @param target url of target URI
     * @param proxy url of proxy
     */
    public static void registerProxyMapping(String target, URL proxy) {
        if (isAlreadyRegistered(target)) {
            return;
        }
        List<Proxy> proxies = new ArrayList<>(1);
        proxies.add(new Proxy(Proxy.Type.HTTP,
                new InetSocketAddress(proxy.getHost(), proxy.getPort())));

        ADDRESSES.add(new Address(target, proxies));
        LOGGER.info("Proxy mapping registered [target: {}, proxy: http://{}:{}]",
                target, proxy.getHost(), proxy.getPort());
    }

    @Override
    public List<Proxy> select(URI uri) {
        List<Proxy> proxies;

        Address filteredAddress = ADDRESSES.stream()
                .filter(address -> address.matches(uri))
                .findFirst().orElse(null);

        if (filteredAddress != null) {
            proxies = filteredAddress.getProxies();
            LOGGER.debug("Selected proxy '{}' for target '{}'.", proxies.get(0), uri);
        } else if (DEFAULT != null) {
            proxies = DEFAULT.select(uri);
            LOGGER.debug("Default proxy '{}' for target '{}'.", proxies.get(0), uri);
        } else {
            proxies = Collections.singletonList(Proxy.NO_PROXY);
            LOGGER.debug("No proxy for target '{}'.", uri);
        }

        return proxies;
    }

    @Override
    public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
        LOGGER.warn("Failed to connect to: " + uri.toString() + " via: " + sa.toString(), ioe);
    }

    private static boolean isAlreadyRegistered(String target) {
        return ADDRESSES.stream()
                .filter(address -> address.getTarget().equals(target))
                .findFirst().orElse(null) != null;
    }

    private static class Address {

        private final String target;
        private final List<Proxy> proxies;

        public Address(String target, List<Proxy> proxies) {
            this.target = target;
            this.proxies = proxies;
        }

        public boolean matches(URI uri) {
            return uri != null && uri.toString().startsWith(target);
        }

        public String getTarget() {
            return target;
        }

        public List<Proxy> getProxies() {
            return proxies;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 53 * hash + Objects.hashCode(this.target);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Address other = (Address) obj;
            return Objects.equals(this.target, other.target);
        }
    }
}
