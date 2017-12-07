package com.github.mstawowiak.commons.ssl;

import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

/**
 *  Truststore manager which accepts all certificates.
 *
 *  NOT FOR PRODUCTION USE!
 */
public class NaiveTrustManager implements X509TrustManager {

    @Override
    public void checkClientTrusted(X509Certificate[] xcs, String string) {}

    @Override
    public void checkServerTrusted(X509Certificate[] xcs, String string) {}

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }

}
