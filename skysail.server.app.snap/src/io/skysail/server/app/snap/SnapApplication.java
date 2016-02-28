package io.skysail.server.app.snap;

import java.util.Arrays;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.event.EventAdmin;

import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import io.skysail.domain.Identifiable;
import io.skysail.domain.core.Repositories;
import io.skysail.server.app.ApiVersion;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.menus.MenuItemProvider;

@Component(immediate = true)
public class SnapApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {
    
    public static final String LIST_ID = "lid";
    public static final String TODO_ID = "id";
    public static final String APP_NAME = "BpmnModeler";
    
    public  SnapApplication() {
        super("Snap", new ApiVersion(1), Arrays.asList());
        //addToAppContext(ApplicationContextId.IMG, "/static/img/silk/page_link.png");
    }

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    private volatile EventAdmin eventAdmin;

    public SnapApplication(String name, ApiVersion apiVersion, List<Class<? extends Identifiable>>  entityClasses) {
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
        router.attach(new RouteBuilder("/Models/{id}", io.skysail.server.app.snap.ModelResource.class));
        router.attach(new RouteBuilder("/Models/", io.skysail.server.app.snap.PostModelResource.class));
        router.attach(new RouteBuilder("/Models/{id}/", io.skysail.server.app.snap.PutModelResource.class));
        router.attach(new RouteBuilder("/Models", io.skysail.server.app.snap.ModelsResource.class));
        router.attach(new RouteBuilder("", io.skysail.server.app.snap.ModelsResource.class));
    }

    public EventAdmin getEventAdmin() {
        return eventAdmin;
    }

    
}