package io.skysail.server.um.simple.it;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

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
    public void logout_is_successful_for_loggedIn_user() throws IOException {
        login("admin", "skysail");
        Representation rep = logout();
        assertUserIsLoggedOut(rep, "admin");
    }

    @Test
    @Ignore
    public void login_with_proper_credentials_is_successful() throws IOException {
        logout();
        Representation rep = login("admin", "skysail");
        assertUserIsLoggedIn(rep, "admin");
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

    private Representation logout() {
        cr = new ClientResource(getBaseUrl() + "/_logout?targetUri=/");
        return cr.get(MediaType.TEXT_HTML);
    }

    private void assertUserIsLoggedOut(Representation rep, String username) throws IOException {
        assertThat(rep.getText(), containsString("Login form"));
        assertThat(rep.getText(), not(containsString(username)));
        assertThat(cr.getResponse().getCookieSettings().getFirstValue("Credentials"), is(equalTo("")));
    }

    private void assertUserIsLoggedIn(Representation rep, String username) throws IOException {
        assertThat(rep.getText(), not(containsString("Login form")));
        assertThat(rep.getText(), containsString(username));
        assertThat(cr.getResponse().getCookieSettings().getFirstValue("Credentials"), is(not(equalTo(""))));
    }
}
