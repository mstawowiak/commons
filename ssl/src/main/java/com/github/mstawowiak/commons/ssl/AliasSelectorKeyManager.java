package com.github.mstawowiak.commons.ssl;

import java.net.Socket;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.X509ExtendedKeyManager;
import javax.net.ssl.X509KeyManager;

@SuppressWarnings("PMD.UseVarargs")
public class AliasSelectorKeyManager extends X509ExtendedKeyManager {

    private final X509KeyManager sourceKeyManager;
    private final String alias;

    public AliasSelectorKeyManager(X509KeyManager keyManager, String alias) {
        this.sourceKeyManager = keyManager;
        this.alias = alias;
    }

    public String chooseClientAlias(String[] keyTypes, Principal[] issuers, Socket socket) {
        if (alias == null || alias.isEmpty()) {
            return sourceKeyManager.chooseClientAlias(keyTypes, issuers, socket);
        }

        for (String keyType : keyTypes) {
            String[] clientAliases = getClientAliases(keyType, issuers);
            if (clientAliases == null) {
                continue;
            }

            if (Arrays.asList(clientAliases).contains(alias)) {
                return alias;
            }
        }

        return null;
    }

    public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket) {
        if (alias == null || alias.isEmpty()) {
            return sourceKeyManager.chooseServerAlias(keyType, issuers, socket);
        }

        String[] serverAliases = getServerAliases(keyType, issuers);
        if (serverAliases == null || !Arrays.asList(serverAliases).contains(alias)) {
            return null;
        }

        return alias;
    }

    public X509Certificate[] getCertificateChain(String alias) {
        return sourceKeyManager.getCertificateChain(alias);
    }

    @Override
    public String[] getClientAliases(String keyType, Principal[] issuers) {
        return sourceKeyManager.getClientAliases(keyType, issuers);
    }

    public PrivateKey getPrivateKey(String alias) {
        return sourceKeyManager.getPrivateKey(alias);
    }

    public String[] getServerAliases(String keyType, Principal[] issuers) {
        return sourceKeyManager.getServerAliases(keyType, issuers);
    }

    @Override
    public String chooseEngineClientAlias(String[] keyTypes, Principal[] issuers, SSLEngine engine) {
        return chooseClientAlias(keyTypes, issuers, null);
    }

    @Override
    public String chooseEngineServerAlias(String keyType, Principal[] issuers, SSLEngine engine) {
        return chooseServerAlias(keyType, issuers, null);
    }

}
