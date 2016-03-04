package io.skysail.server.app.config;

import java.util.Date;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.resource.ResourceException;

/**
 * generated from postResource.stg
 */
public class PostConfigurationSetResource extends PostEntityServerResource<io.skysail.server.app.config.ConfigurationSet> {

	protected ConfigApplication app;

    public PostConfigurationSetResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new ");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (ConfigApplication) getApplication();
    }

    @Override
    public io.skysail.server.app.config.ConfigurationSet createEntityTemplate() {
        return new ConfigurationSet();
    }

    @Override
    public void addEntity(io.skysail.server.app.config.ConfigurationSet entity) {
        Subject subject = SecurityUtils.getSubject();
        entity.setOwner(subject.getPrincipal().toString());
        String id = app.getRepository(io.skysail.server.app.config.ConfigurationSet.class).save(entity, app.getApplicationModel()).toString();
        entity.setId(id);

    }

    @Override
    public String redirectTo() {
        return super.redirectTo(ConfigurationSetsResource.class);
    }
}