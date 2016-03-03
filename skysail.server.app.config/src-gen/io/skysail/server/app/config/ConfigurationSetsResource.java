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
public class ConfigurationSetsResource extends ListServerResource<io.skysail.server.app.config.ConfigurationSet> {

    private ConfigApplication app;
    private ConfigurationSetRepository repository;

    public ConfigurationSetsResource() {
        super(ConfigurationSetResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list ConfigurationSets");
    }

    public ConfigurationSetsResource(Class<? extends ConfigurationSetResource> cls) {
        super(cls);
    }

    @Override
    protected void doInit() {
        app = (ConfigApplication) getApplication();
        repository = (ConfigurationSetRepository) app.getRepository(io.skysail.server.app.config.ConfigurationSet.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<io.skysail.server.app.config.ConfigurationSet> getEntity() {
        Filter filter = new Filter(getRequest());
        Pagination pagination = new Pagination(getRequest(), getResponse(), repository.count(filter));
        return repository.find(filter, pagination);
    }

    @Override
    public List<Link> getLinks() {
              return super.getLinks(PostConfigurationSetResource.class,ConfigurationSetsResource.class,ConfigurationEntrysResource.class);
    }
}