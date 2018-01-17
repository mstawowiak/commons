package com.github.mstawowiak.commons.ssl;

import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link SSLKeystore}
 */
public class SSLKeystoreTest {

    @Test
    public void shouldReadKeystoreFromFile() throws KeyStoreException,
            NoSuchAlgorithmException, UnrecoverableKeyException {

        String path = "src/test/resources/ssl/keystore.jks";
        String password = "123456";
        char[] passwordAsCharArray = password.toCharArray();
        KeyStoreType type = KeyStoreType.JKS;

        String alias = "mykey";

        SSLKeystore keystore = SSLKeystore.fromFile(path, password, type.name());

        Assert.assertEquals(path, keystore.getPath());
        Assert.assertEquals(type.name(), keystore.getType());
        Assert.assertArrayEquals(passwordAsCharArray, keystore.getPassword());

        Assert.assertNotNull(keystore.getKeyStore());
        Assert.assertTrue(keystore.getKeyStore().isKeyEntry(alias));
        Assert.assertEquals(type.name(), keystore.getKeyStore().getType());
        Assert.assertEquals("PKCS#8", keystore.getKeyStore().getKey(alias, passwordAsCharArray).getFormat());
    }

    @Test
    public void shouldReadTruststoreFromFile() throws KeyStoreException,
            NoSuchAlgorithmException, UnrecoverableKeyException {

        String path = "src/test/resources/ssl/truststore.jks";
        String password = "123456";
        char[] passwordAsCharArray = password.toCharArray();
        KeyStoreType type = KeyStoreType.JKS;

        String alias = "mykey";

        SSLKeystore truststore = SSLKeystore.fromFile(path, password, type.name());

        Assert.assertEquals(path, truststore.getPath());
        Assert.assertEquals(type.name(), truststore.getType());
        Assert.assertArrayEquals(passwordAsCharArray, truststore.getPassword());

        Assert.assertNotNull(truststore.getKeyStore());
        Assert.assertTrue(truststore.getKeyStore().isCertificateEntry(alias));
        Assert.assertEquals(type.name(), truststore.getKeyStore().getType());
        Assert.assertEquals("X.509", truststore.getKeyStore().getCertificate(alias).getType());
        Assert.assertEquals("RSA", truststore.getKeyStore().getCertificate(alias).getPublicKey().getAlgorithm());
    }

}
