package com.github.mstawowiak.commons.ssl;

import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class SSLKeystoreSystemPropertiesTest {

    private static String alias = "mykey";
    private static String password = "123456";
    private static KeyStoreType type = KeyStoreType.JKS;

    @BeforeClass
    public static void setUpClass() {
        System.setProperty(SSLKeystore.KEY_STORE_PROPERTY, "src/test/resources/ssl/keystore.jks");
        System.setProperty(SSLKeystore.KEY_STORE_PROPERTY + "Password", password);
        System.setProperty(SSLKeystore.KEY_STORE_PROPERTY + "Type", type.name());

        System.setProperty(SSLKeystore.TRUST_STORE_PROPERTY, "src/test/resources/ssl/truststore.jks");
        System.setProperty(SSLKeystore.TRUST_STORE_PROPERTY + "Password", password);
        System.setProperty(SSLKeystore.TRUST_STORE_PROPERTY + "Type", type.name());
    }

    @AfterClass
    public static void tearDownClass() {
        System.clearProperty(SSLKeystore.KEY_STORE_PROPERTY);
        System.clearProperty(SSLKeystore.KEY_STORE_PROPERTY + "Password");
        System.clearProperty(SSLKeystore.KEY_STORE_PROPERTY + "Type");

        System.clearProperty(SSLKeystore.TRUST_STORE_PROPERTY);
        System.clearProperty(SSLKeystore.TRUST_STORE_PROPERTY + "Password");
        System.clearProperty(SSLKeystore.TRUST_STORE_PROPERTY + "Type");
    }

    @Test
    public void shouldReadKeystoreFromSystemProperties() throws KeyStoreException,
            NoSuchAlgorithmException, UnrecoverableKeyException {

        SSLKeystore keystore = SSLKeystore.keystoreFromSystemProperties();

        Assert.assertEquals(type.name(), keystore.getType());

        Assert.assertNotNull(keystore.getKeyStore());
        Assert.assertTrue(keystore.getKeyStore().isKeyEntry(alias));
        Assert.assertEquals(type.name(), keystore.getKeyStore().getType());
    }

    @Test
    public void shouldReadTruststoreFromSystemProperties() throws KeyStoreException,
            NoSuchAlgorithmException, UnrecoverableKeyException {

        SSLKeystore truststore = SSLKeystore.truststoreFromSystemProperties();

        Assert.assertEquals(type.name(), truststore.getType());

        Assert.assertNotNull(truststore.getKeyStore());
        Assert.assertTrue(truststore.getKeyStore().isCertificateEntry(alias));
        Assert.assertEquals(type.name(), truststore.getKeyStore().getType());
    }
}
