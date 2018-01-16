package com.github.mstawowiak.commons.rest.client.config;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link BasicAuthentication}
 */
public class BasicAuthenticationTest {

    private String username;
    private String password;
    private String authorization;

    @Before
    public void beforeTest() {
        username = "user";
        password = "pass";
        authorization = "Basic dXNlcjpwYXNz";
    }

    @Test
    public void testCreateNewBasicAuthentication() {
        BasicAuthentication basicAuthentication = new BasicAuthentication(username, password);

        Assert.assertNotNull(basicAuthentication);
        Assert.assertEquals(username, basicAuthentication.getUsername());
        Assert.assertArrayEquals(password.toCharArray(), basicAuthentication.getPassword());
        Assert.assertEquals(authorization, basicAuthentication.getAuthorization());
        Assert.assertEquals("BasicAuthentication [username: user, password: ***]", basicAuthentication.toString());
    }

    @Test
    public void testCreateNewBasicAuthenticationWithCharArray() {
        BasicAuthentication basicAuthentication = new BasicAuthentication(username, password.toCharArray());

        Assert.assertNotNull(basicAuthentication);
        Assert.assertEquals(username, basicAuthentication.getUsername());
        Assert.assertArrayEquals(password.toCharArray(), basicAuthentication.getPassword());
        Assert.assertEquals(authorization, basicAuthentication.getAuthorization());
        Assert.assertEquals("BasicAuthentication [username: user, password: ***]", basicAuthentication.toString());
    }

    @Test
    public void testCreateFromAuthorizationHeader() {
        BasicAuthentication basicAuthentication = BasicAuthentication.fromAuthorization(authorization);

        Assert.assertNotNull(basicAuthentication);
        Assert.assertEquals(username, basicAuthentication.getUsername());
        Assert.assertArrayEquals(password.toCharArray(), basicAuthentication.getPassword());
        Assert.assertEquals(authorization, basicAuthentication.getAuthorization());
    }

    @Test
    public void testCreateFromAuthorizationHeaderComplicatedData() {
        String anotherAuthorization = "Basic Y29tcGxpY2F0ZWQyNFVzZXJIb0hvITpjb21wbGljYXRlZDI5ODdAITIiLlBhc1M=";
        BasicAuthentication basicAuthentication = BasicAuthentication.fromAuthorization(anotherAuthorization);

        Assert.assertNotNull(basicAuthentication);
        Assert.assertEquals("complicated24UserHoHo!", basicAuthentication.getUsername());
        Assert.assertEquals("complicated2987@!2\".PasS", String.copyValueOf(basicAuthentication.getPassword()));
        Assert.assertEquals(anotherAuthorization, basicAuthentication.getAuthorization());
        Assert.assertEquals("BasicAuthentication [username: complicated24UserHoHo!, password: ***]", basicAuthentication.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateBasicAuthenticationNullUsername() {
        new BasicAuthentication(null, password);

        Assert.fail();
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateBasicAuthenticationNullStringPassword() {
        String passwordNullString = null;

        new BasicAuthentication(username, passwordNullString);

        Assert.fail();
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateBasicAuthenticationNullCharArrayPassword() {
        char[] passwordNullCharArray = null;

        new BasicAuthentication(username, passwordNullCharArray);

        Assert.fail();
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenAuthorizationStringIsNull() {
        BasicAuthentication.fromAuthorization(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenAuthorizationHasWrongFormat() {
        BasicAuthentication.fromAuthorization("dXNlcjpwYXNz");
    }

}
