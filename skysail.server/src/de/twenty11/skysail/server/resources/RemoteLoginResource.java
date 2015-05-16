package de.twenty11.skysail.server.resources;

import io.skysail.api.responses.FormResponse;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.apache.shiro.SecurityUtils;
import org.restlet.data.CookieSetting;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.app.SkysailRootApplication;
import de.twenty11.skysail.server.core.restlet.utils.CookiesUtils;
import de.twenty11.skysail.server.domain.Credentials;

public class RemoteLoginResource extends PostEntityServerResource<Credentials> {

    @Get("htmlform")
    public FormResponse<Credentials> createForm() {
        return new FormResponse<Credentials>(getEntity(), SkysailRootApplication.PEERS_LOGIN_PATH);
    }

    @Override
    public Credentials getEntity() {
        return new Credentials();
    }

    @Override
    public Credentials getData(Form form) {
        if (form == null) {
            return new Credentials();
        }
        return new Credentials(form.getFirstValue("username"), form.getFirstValue("password"));
    }

    @Override
    protected void doInit() throws ResourceException {
    }

    @Override
    public Credentials createEntityTemplate() {
        return new Credentials();
    }

    @Override
    public SkysailResponse<?> addEntity(Credentials entity) {

        String installation = CookiesUtils.getInstallationFromCookie(getRequest());
        String peersCredentialsName = "Credentials_" + installation;
        
        ClientResource loginCr = new ClientResource("http://todos.int.skysail.io/_login");
        loginCr.setFollowingRedirects(true);
        Form form = new Form();
        form.add("username", entity.getUsername());
        form.add("password", entity.getPassword());
        loginCr.post(form, MediaType.TEXT_HTML);
        String credentials = loginCr.getResponse().getCookieSettings().getFirstValue("Credentials");

        CookieSetting credentialsCookie = new CookieSetting(peersCredentialsName, credentials);
        credentialsCookie.setAccessRestricted(true);
        credentialsCookie.setPath("/");
        getResponse().getCookieSettings().add(credentialsCookie);
        // cr.getCookies().add("Credentials", credentials);
        return null;
    }

    @Override
    public String redirectTo() {
        boolean authenticated = SecurityUtils.getSubject().isAuthenticated();
        if (authenticated) {
            return super.redirectTo(DefaultResource.class);
        }
        return super.redirectTo(RemoteLoginResource.class);
    }

}
