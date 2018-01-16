package com.github.mstawowiak.commons.rest.client.config;

import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link WebserviceClientSettings}
 */
public class WebserviceClientSettingsTest {

    private String url;

    @Before
    public void beforeTest() {
        url = "http://localhost:8081";
    }

    @Test
    public void testBuild() {
        WebserviceClientSettings result = new WebserviceClientSettings.Builder(url).build();

        Assert.assertNotNull(result);
        Assert.assertEquals(url, result.getUrl());
        Assert.assertEquals(WebserviceClientSettings.DEFAULT_CONNECT_TIMEOUT, result.getConnectTimeout());
        Assert.assertEquals(WebserviceClientSettings.DEFAULT_REQUEST_TIMEOUT, result.getRequestTimeout());
    }

    @Test
    public void testBuildNoneAuth() {
        String url = "http://none.host.com:8081";
        WebserviceClientSettings result = new WebserviceClientSettings.Builder(url).build();

        Assert.assertNotNull(result);
        Assert.assertEquals(url, result.getUrl());
        Assert.assertEquals(WebserviceClientSettings.DEFAULT_CONNECT_TIMEOUT, result.getConnectTimeout());
        Assert.assertEquals(WebserviceClientSettings.DEFAULT_REQUEST_TIMEOUT, result.getRequestTimeout());
        Assert.assertNull(result.getProxy());
        Assert.assertNull(result.getBasicAuthentication());
        Assert.assertNull(result.getSslConfiguration());
        Assert.assertTrue(result.getRegisteredComponents().isEmpty());
    }

    @Test
    public void testBuildCustomTimeouts() {
        String url = "http://none.host.com:8081";
        WebserviceClientSettings result = new WebserviceClientSettings.Builder(url)
                .connectTimeout(2)
                .requestTimeout(3)
                .build();

        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.getConnectTimeout());
        Assert.assertEquals(3, result.getRequestTimeout());
    }

    @Test
    public void testBuildWithBasicAuthentication() {
        String url = "http://basic.host.com:8081";
        String username = "user";
        String password = "pass";
        WebserviceClientSettings result = new WebserviceClientSettings.Builder(url)
                .basicAuthentication(username, password).build();

        Assert.assertNotNull(result);
        Assert.assertEquals(url, result.getUrl());
        Assert.assertEquals(username, result.getBasicAuthentication().getUsername());
        Assert.assertArrayEquals(password.toCharArray(), result.getBasicAuthentication().getPassword());
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildWithBasicAuthenticationNullUsername() {
        String url = "http://basic.host.com:8081";
        String username = null;
        String password = "pass";

        new WebserviceClientSettings.Builder(url)
                .basicAuthentication(username, password).build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildWithBasicAuthenticationNullPassword() {
        String url = "http://basic.host.com:8081";
        String username = "user";
        String password = null;

        new WebserviceClientSettings.Builder(url)
                .basicAuthentication(username, password).build();
    }

    @Test
    public void testBuildWithRegisteredComponents() {
        Object componentA = new Object();
        Object componentB = new Object();

        WebserviceClientSettings result = new WebserviceClientSettings.Builder(url)
                .registerComponent(componentA)
                .registerComponent(componentB)
                .build();

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getRegisteredComponents());
        Assert.assertEquals(2, result.getRegisteredComponents().size());
        Assert.assertTrue(result.getRegisteredComponents().contains(componentA));
        Assert.assertTrue(result.getRegisteredComponents().contains(componentB));
    }

    @Test(expected = NullPointerException.class)
    public void testIfBuildWithNullUrl() {
        new WebserviceClientSettings.Builder(null);
    }

    @Test
    public void testIsHttpsUrlYes() {
        String url = "https://localhost:8081";
        WebserviceClientSettings.Builder builder
                = new WebserviceClientSettings.Builder(url);

        Assert.assertNotNull(builder);
        Assert.assertTrue(builder.isHttpsUrl());
    }

    @Test
    public void testIsHttpsUrlNo() {
        WebserviceClientSettings.Builder builder
                = new WebserviceClientSettings.Builder(url);

        Assert.assertNotNull(builder);
        Assert.assertFalse(builder.isHttpsUrl());
    }

    @Test
    public void testIsHttpsUrlMalformedURL() {
        String url = "bla bla bla";
        WebserviceClientSettings.Builder builder
                = new WebserviceClientSettings.Builder(url);

        Assert.assertNotNull(builder);
        Assert.assertFalse(builder.isHttpsUrl());
    }

    @Test
    public void testIsHttpsUrlNullEmpty() {
        String url = "";
        WebserviceClientSettings.Builder builder
                = new WebserviceClientSettings.Builder(url);

        Assert.assertNotNull(builder);
        Assert.assertFalse(builder.isHttpsUrl());
    }

    @Test
    public void testPropertiesWholeMap() {
        Map<String, String> properties = new HashMap<String, String>() {{
            put("param", "value");
            put("param2", "value2");
            put("param3", "value3");
        }};

        WebserviceClientSettings result = new WebserviceClientSettings.Builder(url)
                .properties(properties)
                .build();

        Assert.assertEquals("value", result.getProperties().get("param"));
        Assert.assertEquals("value2", result.getProperties().get("param2"));
        Assert.assertEquals("value3", result.getProperties().get("param3"));
    }

    @Test
    public void testPropertiesBySinglePropertySet() {
        WebserviceClientSettings result = new WebserviceClientSettings.Builder(url)
                .property("param", "value")
                .property("param2", "value2")
                .property("param3", "value3")
                .build();

        Assert.assertEquals("value", result.getProperties().get("param"));
        Assert.assertEquals("value2", result.getProperties().get("param2"));
        Assert.assertEquals("value3", result.getProperties().get("param3"));
    }

    @Test
    public void testPropertiesNullName() {
        WebserviceClientSettings result = new WebserviceClientSettings.Builder(url)
                .property(null, "value")
                .build();

        Assert.assertEquals("value", result.getProperties().get(null));
    }

    @Test
    public void testPropertiesNullValue() {
        WebserviceClientSettings result = new WebserviceClientSettings.Builder(url)
                .property("param", null)
                .build();

        Assert.assertEquals(null, result.getProperties().get("param"));
    }

    @Test
    public void testNoPropertiesSetNull() {
        WebserviceClientSettings result = new WebserviceClientSettings.Builder(url)
                .properties(null)
                .build();

        Assert.assertTrue(result.getProperties().isEmpty());
    }

    @Test
    public void testNoProperties() {
        WebserviceClientSettings result = new WebserviceClientSettings.Builder(url)
                .build();

        Assert.assertTrue(result.getProperties().isEmpty());
    }

}
