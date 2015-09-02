package io.skysail.server.um.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import io.skysail.api.um.AuthenticationService;
import io.skysail.api.um.AuthorizationService;
import io.skysail.server.db.DbService;
import io.skysail.server.um.SkysailUserManagementProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SkysailUserManagementProviderTest {
    @Mock
    private DbService dbService;
    @InjectMocks
    private SkysailUserManagementProvider skysailUserManagementProvider;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void activation_creates_authenticationService() throws Exception {
        skysailUserManagementProvider.activate();
        AuthenticationService authenticationService = skysailUserManagementProvider.getAuthenticationService();
        assertThat(authenticationService, is(notNullValue()));
    }

    @Test
    public void deactivation_nulls_authenticationService() throws Exception {
        skysailUserManagementProvider.activate();
        skysailUserManagementProvider.deactivate();
        AuthenticationService authenticationService = skysailUserManagementProvider.getAuthenticationService();
        assertThat(authenticationService, is(nullValue()));
    }

    @Test
    public void activation_creates_authorizationService() throws Exception {
        skysailUserManagementProvider.activate();
        AuthorizationService service = skysailUserManagementProvider.getAuthorizationService();
        assertThat(service, is(notNullValue()));
    }

    @Test
    public void deactivation_nulls_authorizationService() throws Exception {
        skysailUserManagementProvider.activate();
        skysailUserManagementProvider.deactivate();
        AuthorizationService service = skysailUserManagementProvider.getAuthorizationService();
        assertThat(service, is(nullValue()));
    }

}
