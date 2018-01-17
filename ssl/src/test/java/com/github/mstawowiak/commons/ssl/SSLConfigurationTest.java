package com.github.mstawowiak.commons.ssl;

import javax.net.ssl.SSLContext;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for {@link SSLConfiguration}
 */
public class SSLConfigurationTest {

    private static String keystorePath;
    private static String keystorePassword;
    private static String truststorePath;
    private static String truststorePassword;

    @BeforeClass
    public static void beforeClass() {
        keystorePath  = "src/test/resources/ssl/keystore.jks";
        keystorePassword = "123456";

        truststorePath  = "src/test/resources/ssl/truststore.jks";
        truststorePassword = "123456";
    }

    @Test
    public void shouldCreateSSLContextFromSSLConfiguration() {
        SSLKeystore keystore = SSLKeystore.fromFile(keystorePath, keystorePassword, KeyStoreType.JKS.name());
        SSLKeystore truststore = SSLKeystore.fromFile(truststorePath, truststorePassword, KeyStoreType.JKS.name());

        SSLConfiguration sslConfiguration =
                new SSLConfiguration.Builder()
                        .keystore(keystore)
                        .truststore(truststore)
                        .build();

        Assert.assertNotNull(sslConfiguration);
        Assert.assertEquals(TLSProtocol.TLS_v1_2, sslConfiguration.getProtocol());
        Assert.assertTrue(sslConfiguration.isVerifyCertificate());
        Assert.assertTrue(sslConfiguration.isVerifyHostname());

        SSLContext sslContext = sslConfiguration.createSSLContext();

        Assert.assertNotNull(sslContext);
        Assert.assertEquals(TLSProtocol.TLS_v1_2.getValue(), sslContext.getProtocol());

        System.out.println(sslConfiguration.toString());
    }
}
