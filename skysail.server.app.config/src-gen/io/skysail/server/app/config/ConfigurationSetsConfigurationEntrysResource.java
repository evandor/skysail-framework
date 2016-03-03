package io.skysail.server.app.config;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.db.DbClassName;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;

/**
 * generated from listResource.stg
 */
public class ConfigurationSetsConfigurationEntrysResource extends ListServerResource<ConfigurationEntry> {

    private ConfigApplication app;
    private ConfigurationEntryRepository oeRepo;

    public ConfigurationSetsConfigurationEntrysResource() {
        super(ConfigurationSetResource.class, ConfigurationSetsConfigurationSetResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "["+this.getClass().getSimpleName()+"]");
    }

    @Override
    protected void doInit() {
        app = (ConfigApplication) getApplication();
        oeRepo = (Repository) app.getRepository(ConfigurationEntry.class);
    }

    @Override
    public List<ConfigurationEntry> getEntity() {
        return (List<ConfigurationEntry>) oeRepo.execute(ConfigurationEntry.class, "select * from " + DbClassName.of(ConfigurationEntry.class) + " where #"+getAttribute("id")+" in IN(folders)");
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(ConfigurationSetsConfigurationEntrysResource.class, PostConfigurationSetsConfigurationEntryRelationResource.class);
    }
}