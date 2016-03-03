package io.skysail.server.app.config;
import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;

/**
 * generated from targetRelationResource.stg
 */
public class ConfigurationSetsConfigurationEntryResource extends EntityServerResource<ConfigurationEntry> {

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

    @Override
    public ConfigurationEntry getEntity() {
        return null;
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(ConfigurationSetsConfigurationEntrysResource.class, PostConfigurationSetsConfigurationEntryRelationResource.class);
    }

}