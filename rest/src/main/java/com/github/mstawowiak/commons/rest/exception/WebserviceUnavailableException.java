package com.github.mstawowiak.commons.rest.exception;

public class WebserviceUnavailableException extends RuntimeException {

    public WebserviceUnavailableException(String message) {
        super(message);
    }

    public WebserviceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
