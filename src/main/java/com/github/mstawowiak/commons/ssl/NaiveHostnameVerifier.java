package com.github.mstawowiak.commons.ssl;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 *  A {@link HostnameVerifier} which always verifies a hostname as true.
 *
 *  NOT FOR PRODUCTION USE!
 */
public class NaiveHostnameVerifier implements HostnameVerifier {

    @Override
    public boolean verify(final String hostname, final SSLSession session) {
        return true;
    }
}
