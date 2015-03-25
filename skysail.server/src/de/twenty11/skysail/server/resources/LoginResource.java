package de.twenty11.skysail.server.resources;

import io.skysail.api.responses.FormResponse;
import io.skysail.api.responses.SkysailResponse;

import org.apache.shiro.SecurityUtils;
import org.restlet.data.Form;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.app.SkysailRootApplication;
import de.twenty11.skysail.server.core.restlet.PostEntityServerResource;
import de.twenty11.skysail.server.domain.Credentials;

public class LoginResource extends PostEntityServerResource<Credentials> {

    @Get("htmlform")
    public FormResponse<Credentials> createForm() {
        return new FormResponse<Credentials>(getEntity(), SkysailRootApplication.LOGIN_PATH);
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
        return null;
    }
    
    @Override
    public String redirectTo() {
        boolean authenticated = SecurityUtils.getSubject().isAuthenticated();
        if (authenticated) {
            return super.redirectTo(WelcomeResource.class);
        }
        return super.redirectTo(LoginResource.class);
    }

}
