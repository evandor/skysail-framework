package io.skysail.server.app.grapesjs;

import java.util.*;

import org.osgi.service.component.annotations.*;
import org.osgi.service.event.EventAdmin;

import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import io.skysail.domain.Identifiable;
import io.skysail.domain.core.Repositories;
import io.skysail.server.app.*;
import io.skysail.server.menus.MenuItemProvider;

@Component(immediate = true)
public class GrapesJsApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {
    
    public static final String LIST_ID = "lid";
    public static final String TODO_ID = "id";
    public static final String APP_NAME = "BpmnModeler";
    
    public  GrapesJsApplication() {
        super("GrapesJs", new ApiVersion(1), Arrays.asList());
        //addToAppContext(ApplicationContextId.IMG, "/static/img/silk/page_link.png");
    }

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    private volatile EventAdmin eventAdmin;

    public GrapesJsApplication(String name, ApiVersion apiVersion, List<Class<? extends Identifiable>>  entityClasses) {
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
        router.attach(new RouteBuilder("/Models/{id}", io.skysail.server.app.grapesjs.ModelResource.class));
        router.attach(new RouteBuilder("/Models/", io.skysail.server.app.grapesjs.PostModelResource.class));
        router.attach(new RouteBuilder("/Models/{id}/", io.skysail.server.app.grapesjs.PutModelResource.class));
        router.attach(new RouteBuilder("/Models", io.skysail.server.app.grapesjs.ModelsResource.class));
        router.attach(new RouteBuilder("", io.skysail.server.app.grapesjs.ModelsResource.class));
    }

    public EventAdmin getEventAdmin() {
        return eventAdmin;
    }

    
}