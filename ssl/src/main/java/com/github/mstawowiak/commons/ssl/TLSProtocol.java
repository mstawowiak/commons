package com.github.mstawowiak.commons.ssl;

public enum TLSProtocol {

    TLS("TLS"),
    TLS_v1("TLSv1"),
    TLS_v1_1("TLSv1.1"),
    TLS_v1_2("TLSv1.2");

    private final String value;

    TLSProtocol(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static TLSProtocol getDefault() {
        return TLS_v1_2;
    }
}
