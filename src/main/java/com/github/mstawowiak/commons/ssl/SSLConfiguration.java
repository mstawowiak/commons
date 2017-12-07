package com.github.mstawowiak.commons.ssl;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.util.Collections;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509KeyManager;

public class SSLConfiguration {

    private final SSLKeystore keystore;
    private final SSLKeystore truststore;

    private final TLSProtocol protocol;

    private final String keyAlias;

    private final boolean verifyCertificate;
    private final boolean verifyHostname;

    private SSLConfiguration(SSLConfiguration.Builder builder) {
        this.keystore = builder.keystore;
        this.truststore = builder.truststore;
        this.protocol = builder.protocol;
        this.keyAlias = builder.keyAlias;
        this.verifyCertificate = builder.verifyCertificate;
        this.verifyHostname = builder.verifyHostname;
    }

    public static class Builder {

        private SSLKeystore keystore;
        private SSLKeystore truststore;

        private TLSProtocol protocol = TLSProtocol.getDefault();

        private String keyAlias;

        private boolean verifyCertificate = true;
        private boolean verifyHostname = true;

        public Builder keystore(SSLKeystore keystore) {
            this.keystore = keystore;
            return this;
        }

        public Builder truststore(SSLKeystore truststore) {
            this.truststore = truststore;
            return this;
        }

        public Builder protocol(TLSProtocol protocol) {
            this.protocol = protocol;
            return this;
        }

        public Builder keyAlias(String keyAlias) {
            this.keyAlias = keyAlias;
            return this;
        }

        public Builder verifyCertificate(boolean verifyCertificate) {
            this.verifyCertificate = verifyCertificate;
            return this;
        }

        public Builder verifyHostname(boolean verifyHostname) {
            this.verifyHostname = verifyHostname;
            return this;
        }

        @SuppressWarnings("PMD.AccessorClassGeneration")
        public SSLConfiguration build() {
            return new SSLConfiguration(this);
        }
    }

    public SSLKeystore getKeystore() {
        return keystore;
    }

    public SSLKeystore getTruststore() {
        return truststore;
    }

    public TLSProtocol getProtocol() {
        return protocol;
    }

    public boolean isVerifyCertificate() {
        return verifyCertificate;
    }

    public boolean isVerifyHostname() {
        return verifyHostname;
    }

    public SSLContext createSSLContext() {
        SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance(protocol.getValue());
        } catch (NoSuchAlgorithmException ex) {
            throw new SSLConfigurationException("Failed to get instance of SSLContext: " + ex.getMessage(), ex);
        }

        KeyManager[] keyManagers = createKeyManagers();
        TrustManager[] trustManagers = createTrustManagers();

        try {
            sslContext.init(keyManagers,trustManagers, new SecureRandom());
        } catch (KeyManagementException ex) {
            throw new SSLConfigurationException("Failed to initialize SSL context: " + ex.getMessage(), ex);
        }

        return sslContext;
    }

    private KeyManager[] createKeyManagers() {
        if (keystore == null) {
            return null;
        }
        try {
            KeyManagerFactory factory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            factory.init(keystore.getKeyStore(), keystore.getPassword());

            KeyManager[] keyManagers = factory.getKeyManagers();
            for (int i = 0; i < keyManagers.length; i++) {
                KeyManager manager = keyManagers[i];

                if (manager instanceof X509KeyManager) {
                    keyManagers[i++] = new AliasSelectorKeyManager((X509KeyManager) manager, keyAlias);
                }
            }

            return keyManagers;
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException ex) {
            throw new SSLConfigurationException("Failed to set keystore: " + ex.getMessage(), ex);
        }
    }

    private TrustManager[] createTrustManagers() {
        if (!verifyCertificate) {
           return new TrustManager[]{new NaiveTrustManager()};
        } else if (truststore == null) {
            return null;
        }

        try {
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(truststore.getKeyStore());
            return tmf.getTrustManagers();
        } catch (KeyStoreException | NoSuchAlgorithmException e) {
            throw new SSLConfigurationException("Failed to set truststore: " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        return new StringBuilder("SSLConfiguration [").append(System.lineSeparator())
                .append("keyStore: ").append(getKeyStoreAsString())
                .append("trustStore: ").append(getTrustStoreAsString())
                .append("protocol: ").append(protocol.getValue()).append(System.lineSeparator())
                .append("keyAlias: ").append(keyAlias != null ? keyAlias : "").append(System.lineSeparator())
                .append("verifyCertificate: ").append(verifyCertificate).append(System.lineSeparator())
                .append("verifyHostname: ").append(verifyHostname)
                .append(']').toString();
    }

    @SuppressWarnings("PMD.EmptyCatchBlock")
    private String getKeyStoreAsString() {
        StringBuilder sb = new StringBuilder(100);

        if (keystore != null) {
            sb.append("KeyStore [");
            if (keystore.getPath() != null) {
                sb.append("path: ").append(keystore.getPath()).append(", ");
            }
            sb.append("type: ").append(keystore.getType());

            try {
                String aliases = String.join(", ", Collections.list(keystore.getKeyStore().aliases()));
                sb.append(", aliases: ").append(aliases);
            } catch (KeyStoreException ex) {
            }
            sb.append(']').append(System.lineSeparator());
        } else {
            sb.append("no keyStore").append(System.lineSeparator());
        }
        return sb.toString();
    }

    @SuppressWarnings("PMD.EmptyCatchBlock")
    private String getTrustStoreAsString() {
        StringBuilder sb = new StringBuilder(100);

        if (truststore != null) {
            sb.append("TrustStore [");
            if (truststore.getPath() != null) {
                sb.append("path: ").append(truststore.getPath()).append(", ");
            }
            sb.append("type: ").append(truststore.getType());

            try {
                String aliases = String.join(", ", Collections.list(truststore.getKeyStore().aliases()));
                sb.append(", aliases: ").append(aliases);
            } catch (KeyStoreException ex) {
            }
            sb.append(']').append(System.lineSeparator());
        } else {
            sb.append("no trustStore").append(System.lineSeparator());
        }
        return sb.toString();
    }
}
