package io.skysail.server.app.config;

import java.util.*;

import org.osgi.service.component.annotations.*;
import org.osgi.service.event.EventAdmin;

import io.skysail.server.app.*;
import de.twenty11.skysail.server.core.restlet.*;
import io.skysail.domain.Identifiable;
import io.skysail.domain.core.Repositories;
import io.skysail.server.app.*;
import io.skysail.server.menus.MenuItemProvider;

public class ConfigApplicationGen extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String LIST_ID = "lid";
    public static final String TODO_ID = "id";
    public static final String APP_NAME = "Config";

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    private volatile EventAdmin eventAdmin;

    public ConfigApplicationGen(String name, ApiVersion apiVersion, List<Class<? extends Identifiable>>  entityClasses) {
        super(name, apiVersion, entityClasses);
    }

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY)
    public void setRepositories(Repositories repos) {
        super.setRepositories(repos);
    }

    public void unsetRepositories(Repositories repo) {
        super.setRepositories(null);
    }



    @Override
    protected void attach() {
        super.attach();
        router.attach(new RouteBuilder("/ConfigurationSets/{id}", io.skysail.server.app.config.configurationset.resources.ConfigurationSetResourceGen.class));
        router.attach(new RouteBuilder("/ConfigurationSets/", io.skysail.server.app.config.configurationset.resources.PostConfigurationSetResourceGen.class));
        router.attach(new RouteBuilder("/ConfigurationSets/{id}/", io.skysail.server.app.config.configurationset.resources.PutConfigurationSetResourceGen.class));
        router.attach(new RouteBuilder("/ConfigurationSets", io.skysail.server.app.config.configurationset.resources.ConfigurationSetsResourceGen.class));
        router.attach(new RouteBuilder("", io.skysail.server.app.config.configurationset.resources.ConfigurationSetsResourceGen.class));
        router.attach(new RouteBuilder("/ConfigurationSets/{id}/ConfigurationEntrys", io.skysail.server.app.config.configurationset.ConfigurationSetsConfigurationEntrysResource.class));
        router.attach(new RouteBuilder("/ConfigurationSets/{id}/ConfigurationEntrys/", io.skysail.server.app.config.configurationset.PostConfigurationSetToNewConfigurationEntryRelationResource.class));
        router.attach(new RouteBuilder("/ConfigurationSets/{id}/ConfigurationEntrys/{targetId}", io.skysail.server.app.config.configurationset.ConfigurationSetsConfigurationEntryResource.class));
        router.attach(new RouteBuilder("/ConfigurationEntrys/{id}", io.skysail.server.app.config.configurationentry.resources.ConfigurationEntryResourceGen.class));
        router.attach(new RouteBuilder("/ConfigurationEntrys/", io.skysail.server.app.config.configurationentry.resources.PostConfigurationEntryResourceGen.class));
        router.attach(new RouteBuilder("/ConfigurationEntrys/{id}/", io.skysail.server.app.config.configurationentry.resources.PutConfigurationEntryResourceGen.class));
        router.attach(new RouteBuilder("/ConfigurationEntrys", io.skysail.server.app.config.configurationentry.resources.ConfigurationEntrysResourceGen.class));
        router.attach(new RouteBuilder("", io.skysail.server.app.config.configurationentry.resources.ConfigurationEntrysResourceGen.class));

    }

    public EventAdmin getEventAdmin() {
        return eventAdmin;
    }

}