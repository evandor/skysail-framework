package io.skysail.server.app.whatAbout;

import java.util.Arrays;

import org.osgi.service.component.annotations.*;
import org.osgi.service.event.EventAdmin;

import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.*;
import io.skysail.domain.core.Repositories;
import io.skysail.server.app.*;
import io.skysail.server.menus.MenuItemProvider;

@Component(immediate = true)
public class WhatAboutApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String LIST_ID = "lid";
    public static final String TODO_ID = "id";
    public static final String APP_NAME = "WhatAbout";

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    private volatile EventAdmin eventAdmin;

    public WhatAboutApplication() {
        super("WhatAbout", new ApiVersion(1), Arrays.asList());
        addToAppContext(ApplicationContextId.IMG, "/static/img/silk/page_link.png");
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
        router.attach(new RouteBuilder("/io.skysail.server.app.whatAbout.Events/{id}", io.skysail.server.app.whatAbout.EventResource.class));
        router.attach(new RouteBuilder("/io.skysail.server.app.whatAbout.Events/", io.skysail.server.app.whatAbout.PostEventResource.class));
        router.attach(new RouteBuilder("/io.skysail.server.app.whatAbout.Events/{id}/", io.skysail.server.app.whatAbout.PutEventResource.class));
        router.attach(new RouteBuilder("/io.skysail.server.app.whatAbout.Events", io.skysail.server.app.whatAbout.EventsResource.class));
        router.attach(new RouteBuilder("", io.skysail.server.app.whatAbout.EventsResource.class));
        router.attach(new RouteBuilder("/io.skysail.server.app.whatAbout.Sources/{id}", io.skysail.server.app.whatAbout.SourceResource.class));
        router.attach(new RouteBuilder("/io.skysail.server.app.whatAbout.Sources/", io.skysail.server.app.whatAbout.PostSourceResource.class));
        router.attach(new RouteBuilder("/io.skysail.server.app.whatAbout.Sources/{id}/", io.skysail.server.app.whatAbout.PutSourceResource.class));
        router.attach(new RouteBuilder("/io.skysail.server.app.whatAbout.Sources", io.skysail.server.app.whatAbout.SourcesResource.class));
        router.attach(new RouteBuilder("", io.skysail.server.app.whatAbout.SourcesResource.class));

    }

    public EventAdmin getEventAdmin() {
        return eventAdmin;
    }

}