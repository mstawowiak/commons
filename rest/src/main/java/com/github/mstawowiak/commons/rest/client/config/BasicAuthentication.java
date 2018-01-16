package com.github.mstawowiak.commons.rest.client.config;

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Objects;

/**
 * This class is a data holder for basic authentication credentials - username & password
 */
public final class BasicAuthentication {

    public static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    public static final String AUTHORIZATION_HEADER_VALUE_PREFIX = "Basic";

    private final String username;
    private final char[] password;

    /**
     * Creates a new {@code BasicAuthentication} object from the given username and password
     *
     * @param username the username
     * @param password the user's password as char array
     */
    @SuppressWarnings({"PMD.UseVarargs", "PMD.ArrayIsStoredDirectly"})
    public BasicAuthentication(String username, char[] password) {
        if (username == null || password == null) {
            throw new IllegalStateException("Both username and password are required for Basic Authentication.");
        }
        this.username = username;
        this.password = password;
    }

    /**
     * Creates a new {@code BasicAuthentication} object from the given username and password
     *
     * @param username the username
     * @param password the user's password as String
     */
    public BasicAuthentication(String username, String password) {
        this(username, password != null ? password.toCharArray() : null);
    }

    /**
     * Returns the basic authentication username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the basic authentication user password.
     *
     * @return the password
     */
    @SuppressWarnings("PMD.MethodReturnsInternalArray")
    public char[] getPassword() {
        return password;
    }

    /**
     * Returns value for HTTP 'Authorization' header build from username and password
     *
     * @return value for HTTP 'Authorization' header
     */
    public String getAuthorization() {
        String authString = username + ":" + String.copyValueOf(password);
        String authStringEncoded = Base64.getEncoder().encodeToString(authString.getBytes());

        return AUTHORIZATION_HEADER_VALUE_PREFIX + ' ' + authStringEncoded;
    }

    /**
     * Create {@link BasicAuthentication} instance from value of HTTP 'Authorization' header
     *
     * @param authorization value of HTTP 'Authorization' header
     * @return new instance of {@link BasicAuthentication} class
     */
    public static BasicAuthentication fromAuthorization(final String authorization) {
        Objects.requireNonNull(authorization, "Authorization cannot be null");

        if (!authorization.startsWith(AUTHORIZATION_HEADER_VALUE_PREFIX)) {
            throw new IllegalArgumentException("Wrong format of 'Authorization'");
        }

        // Authorization: Basic base64credentials
        String base64Credentials = authorization.substring(AUTHORIZATION_HEADER_VALUE_PREFIX.length()).trim();
        String credentials = new String(Base64.getDecoder().decode(base64Credentials), Charset.forName("UTF-8"));

        // credentials = username:password
        final String[] values = credentials.split(":", 2);
        return new BasicAuthentication(values[0], values[1]);
    }

    @Override
    public String toString() {
        return new StringBuilder("BasicAuthentication [")
                .append("username: ").append(username)
                .append(", password: ***").append(']')
                .toString();
    }
}