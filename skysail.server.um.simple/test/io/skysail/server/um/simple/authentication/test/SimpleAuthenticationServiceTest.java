package io.skysail.server.um.simple.authentication.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import io.skysail.server.um.simple.authentication.SimpleAuthenticationService;

import org.junit.Test;
import org.mockito.Mockito;
import org.restlet.Context;
import org.restlet.security.Authenticator;

public class SimpleAuthenticationServiceTest {

    @Test
    public void testName() {
        SimpleAuthenticationService service = new SimpleAuthenticationService(null);
        Context context = Mockito.mock(Context.class);
        Authenticator authenticator = service.getAuthenticator(context);
        assertThat(authenticator, is(notNullValue()));
    }
}
