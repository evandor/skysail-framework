package io.skysail.server.app.config;

import java.util.List;

import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.PostRelationResource2;

/**
 * generated from postRelationToNewEntityResource.stg
 */
public class PostConfigurationSetToNewConfigurationEntryRelationResource extends PostRelationResource2<ConfigurationEntry> {

    private ConfigApplication app;
    private ConfigurationEntryRepository repo;
    private String parentId;

    public PostConfigurationSetToNewConfigurationEntryRelationResource() {
        // addToContext(ResourceContextId.LINK_TITLE, "add");
    }

    @Override
    protected void doInit() {
        app = (ConfigApplication) getApplication();
        repo = (ConfigurationEntryRepository) app.getRepository(.class);
        parentId = getAttribute("id");
    }

    public ConfigurationEntry createEntityTemplate() {
        return new ConfigurationEntry();
    }

    @Override
    public void addEntity(ConfigurationEntry entity) {
        ConfigurationEntry parent = repo.findOne(parentId);
        parent.getConfigurationEntrys().add(entity);
        repo.save(parent, getApplication().getApplicationModel());
    }
}