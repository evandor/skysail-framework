package io.skysail.server.app.config;

import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.api.links.Link;

import java.util.*;

import io.skysail.server.ResourceContextId;

/**
 * generated from listResource.stg
 */
public class ConfigurationEntrysResource extends ListServerResource<io.skysail.server.app.config.ConfigurationEntry> {

    private ConfigApplication app;
    private ConfigurationEntryRepository repository;

    public ConfigurationEntrysResource() {
        super(ConfigurationEntryResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list ConfigurationEntrys");
    }

    public ConfigurationEntrysResource(Class<? extends ConfigurationEntryResource> cls) {
        super(cls);
    }

    @Override
    protected void doInit() {
        app = (ConfigApplication) getApplication();
        repository = (ConfigurationEntryRepository) app.getRepository(io.skysail.server.app.config.ConfigurationEntry.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<io.skysail.server.app.config.ConfigurationEntry> getEntity() {
        Filter filter = new Filter(getRequest());
        Pagination pagination = new Pagination(getRequest(), getResponse(), repository.count(filter));
        return repository.find(filter, pagination);
    }

    @Override
    public List<Link> getLinks() {
              return super.getLinks(PostConfigurationEntryResource.class,ConfigurationSetsResource.class,ConfigurationEntrysResource.class);
    }
}