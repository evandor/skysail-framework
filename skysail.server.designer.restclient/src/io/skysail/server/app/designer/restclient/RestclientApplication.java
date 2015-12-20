package io.skysail.server.app.designer.restclient;

import org.osgi.service.component.annotations.*;
import org.osgi.service.event.EventAdmin;

import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.ApplicationContextId;
import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.menus.MenuItemProvider;
import lombok.Getter;

@Component(immediate = true)
public class RestclientApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String LIST_ID = "lid";
    public static final String TODO_ID = "id";
    public static final String APP_NAME = "Restclient";
    
    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    @Getter
    private volatile EventAdmin eventAdmin;

    private RestclientRepository repo;

    public RestclientApplication() {
        super(APP_NAME);
        addToAppContext(ApplicationContextId.IMG, "/static/img/silk/page_link.png");
    }

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY, target = "(name=RestclientRepository)")
    public void setRepository(DbRepository repo) {
        this.repo = (RestclientRepository) repo;
    }

    public void unsetRepository(DbRepository repo) {
        this.repo = null;
    }

//    public Repository getRepository() {
//        return (Repository)repo;
//    }

    @Override
    protected void attach() {
        super.attach();

    }

}