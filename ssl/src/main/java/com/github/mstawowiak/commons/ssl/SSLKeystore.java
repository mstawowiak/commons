package com.github.mstawowiak.commons.ssl;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public final class SSLKeystore {

    static final String KEY_STORE_PROPERTY = "javax.net.ssl.keyStore";
    static final String TRUST_STORE_PROPERTY = "javax.net.ssl.trustStore";

    private final KeyStore keyStore;
    private final char[] password;

    private final String path;

    private SSLKeystore(String path, String password, String type) {
        String keyStoreType = type != null ? type : KeyStore.getDefaultType();

        this.path = path;
        this.password = password.toCharArray();
        try {
            KeyStore loadedKeyStore = KeyStore.getInstance(keyStoreType);

            try (FileInputStream fis = new FileInputStream(path)) {
                loadedKeyStore.load(fis, this.password);
            }

            this.keyStore = loadedKeyStore;
        } catch (CertificateException | IOException | KeyStoreException | NoSuchAlgorithmException e) {
            throw new SSLConfigurationException("Failed to load keystore/truststore from file: " + e.getMessage(), e);
        }
    }

    public static SSLKeystore fromFile(String path, String password, String type) {
        return new SSLKeystore(path, password, type);
    }

    public static SSLKeystore keystoreFromSystemProperties() {
        return fromSystemProperties(KEY_STORE_PROPERTY);
    }

    public static SSLKeystore truststoreFromSystemProperties() {
        return fromSystemProperties(TRUST_STORE_PROPERTY);
    }

    private static SSLKeystore fromSystemProperties(String property) {
        return new SSLKeystore(
                System.getProperty(property),
                System.getProperty(property + "Password"),
                System.getProperty(property + "Type"));
    }

    public KeyStore getKeyStore() {
        return keyStore;
    }

    public String getType() {
        return keyStore.getType();
    }

    @SuppressWarnings("PMD.MethodReturnsInternalArray")
    public char[] getPassword() {
        return password;
    }

    public String getPath() {
        return path;
    }
}
