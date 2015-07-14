package io.skysail.server.um.simple.it;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.restlet.Client;
import org.restlet.Context;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Parameter;
import org.restlet.data.Protocol;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.util.Series;

public class LoginTests extends IntegrationTests {

    private ClientResource cr;

    @Before
    public void setUp() throws Exception {
        form = new Form();
    }

    @Test
    @Ignore
    public void logout_clears_credentials_cookie() throws IOException {
        Representation rep = logout();
        assertUserIsLoggedOut(rep, "admin");
    }

    @Test
    @Ignore
    public void logout_is_successful_for_loggedIn_user() throws IOException {
        login("admin", "skysail");
        Representation rep = logout();
        assertUserIsLoggedOut(rep, "admin");
    }

    @Test
    @Ignore
    public void login_with_proper_credentials_is_successful() throws IOException {
        logout();
        login("admin", "skysail");
        String credentials = cr.getResponse().getCookieSettings().getFirstValue("Credentials");
        ClientResource clientResource = new ClientResource(getBaseUrl());
        clientResource.getCookies().add("Credentials", credentials);
        Representation rep = clientResource.get(MediaType.TEXT_HTML);
        assertUserIsLoggedIn(rep, "admin");
    }


    @Test
    @Ignore
    public void logging_out_and_logging_in_with_wrong_credentials_is_not_successful() throws Exception {
        logout();
        login("admin", "skysail");
        logout();
        login("admin", "wrong");
        String credentials = cr.getResponse().getCookieSettings().getFirstValue("Credentials");
        ClientResource clientResource = new ClientResource(getBaseUrl());
        clientResource.getCookies().add("Credentials", credentials);
        Representation rep = clientResource.get(MediaType.TEXT_HTML);
        assertUserIsLoggedOut(rep, "admin");
    }

    @Test
    @Ignore
    public void loggedIn_User_loggingIn_again_with_wrong_credentials_is_loggedOut() throws IOException {
        logout();
        login("admin", "skysail");
        Representation rep = login("admin", "wrong");
        assertUserIsLoggedOut(rep, "admin");
    }

    private Representation login(String username, String password) {
        cr = new ClientResource(getBaseUrl() + "/_login");
        cr.setFollowingRedirects(true);
        Form form = new Form();
        form.add("username", username);
        form.add("password", password);
        return cr.post(form, MediaType.TEXT_HTML);
    }

    private Representation login2(String username, String password) {
        Client client = new Client(new Context(), Protocol.HTTP);
        Context ctx = client.getContext();

        Series<Parameter> params = ctx.getParameters();
        params.add("followRedirects", "true");
        ctx.setParameters(params);

        ClientResource cr = new ClientResource(ctx, getBaseUrl() + "/_login");
        Form form = new Form();
        form.add("username", username);
        form.add("password", password);
        return cr.post(form, MediaType.TEXT_HTML);

    }

    private Representation logout() {
        cr = new ClientResource(getBaseUrl() + "/_logout?targetUri=/");
        return cr.get(MediaType.TEXT_HTML);
    }

    private void assertUserIsLoggedOut(Representation rep, String username) throws IOException {
        String text = rep.getText();
        //assertThat(text, containsString("Login form"));
        assertThat(text, not(containsString(username)));
        assertThat(cr.getResponse().getCookieSettings().getFirstValue("Credentials"), is(nullValue()));
    }

    private void assertUserIsLoggedIn(Representation rep, String username) throws IOException {
        String text = rep.getText();
        assertThat(text, not(containsString("Login form")));
        assertThat(text, containsString(username));
        assertThat(cr.getResponse().getCookieSettings().getFirstValue("Credentials"), is(not(equalTo(""))));
    }
}
