package de.twenty11.skysail.server.resources;

import io.skysail.api.responses.*;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.apache.shiro.SecurityUtils;
import org.restlet.data.Form;
import org.restlet.resource.*;

import de.twenty11.skysail.server.app.SkysailRootApplication;
import de.twenty11.skysail.server.domain.Credentials;

public class DemoLoginResource extends PostEntityServerResource<Credentials> {

    @Get("htmlform")
    public FormResponse<Credentials> createForm() {
        return new FormResponse<Credentials>(getEntity(), SkysailRootApplication.DEMO_LOGIN_PATH);
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
    public SkysailResponse<Credentials> addEntity(Credentials entity) {
        return null;
    }

    @Override
    public String redirectTo() {
        boolean authenticated = SecurityUtils.getSubject().isAuthenticated();
        if (authenticated) {
            return "/";//super.redirectTo(DefaultResource.class);
        }
        return super.redirectTo(DemoLoginResource.class);
    }

}