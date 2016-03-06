package io.skysail.server.app.config;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.resources.EntityServerResource;

/**
 * generated from entityResource.stg
 */
public class ConfigurationEntryResource extends EntityServerResource<io.skysail.server.app.config.ConfigurationEntry> {

    private String id;
    private ConfigApplication app;
    private ConfigurationEntryRepository repository;

    public ConfigurationEntryResource() {
        addToContext(ResourceContextId.LINK_TITLE, "details");
        addToContext(ResourceContextId.LINK_GLYPH, "search");
    }

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (ConfigApplication) getApplication();
        repository = (ConfigurationEntryRepository) app.getRepository(io.skysail.server.app.config.ConfigurationEntry.class);
    }


    @Override
    public SkysailResponse<?> eraseEntity() {
        repository.delete(id);
        return new SkysailResponse<>();
    }

    @Override
    public io.skysail.server.app.config.ConfigurationEntry getEntity() {
        return (io.skysail.server.app.config.ConfigurationEntry)app.getRepository().findOne(id);
    }

	@Override
    public List<Link> getLinks() {
        return super.getLinks(PutConfigurationEntryResource.class);
    }

}