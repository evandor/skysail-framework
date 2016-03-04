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
public class PostConfigurationEntryResource extends PostEntityServerResource<io.skysail.server.app.config.ConfigurationEntry> {

	protected ConfigApplication app;

    public PostConfigurationEntryResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new ");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (ConfigApplication) getApplication();
    }

    @Override
    public io.skysail.server.app.config.ConfigurationEntry createEntityTemplate() {
        return new ConfigurationEntry();
    }

    @Override
    public void addEntity(io.skysail.server.app.config.ConfigurationEntry entity) {
        Subject subject = SecurityUtils.getSubject();
        String id = app.getRepository(io.skysail.server.app.config.ConfigurationEntry.class).save(entity, app.getApplicationModel()).toString();
        entity.setId(id);

    }

    @Override
    public String redirectTo() {
        return super.redirectTo(ConfigurationEntrysResource.class);
    }
}