package io.skysail.server.app.config;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import java.util.Date;
import org.restlet.resource.ResourceException;

/**
 * generated from putResource.stg
 */
public class PutConfigurationEntryResource extends PutEntityServerResource<io.skysail.server.app.config.ConfigurationEntry> {


    protected String id;
    protected ConfigApplication app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (ConfigApplication)getApplication();
    }

    @Override
    public void updateEntity(ConfigurationEntry  entity) {
        io.skysail.server.app.config.ConfigurationEntry original = getEntity();
        copyProperties(original,entity);

        app.getRepository(io.skysail.server.app.config.ConfigurationEntry.class).update(original,app.getApplicationModel());
    }

    @Override
    public io.skysail.server.app.config.ConfigurationEntry getEntity() {
        return (io.skysail.server.app.config.ConfigurationEntry)app.getRepository(io.skysail.server.app.config.ConfigurationEntry.class).findOne(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(ConfigurationEntrysResource.class);
    }
}