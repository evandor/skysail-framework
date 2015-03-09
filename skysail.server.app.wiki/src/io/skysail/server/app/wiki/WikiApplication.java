package io.skysail.server.app.wiki;

import io.skysail.server.app.designer.repo.DesignerRepository;
import io.skysail.server.app.wiki.application.RootResource;

import java.util.Arrays;
import java.util.List;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.app.SkysailApplication;
import de.twenty11.skysail.server.core.db.DbRepository;
import de.twenty11.skysail.server.core.restlet.ApplicationContextId;
import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import de.twenty11.skysail.server.services.MenuItem;
import de.twenty11.skysail.server.services.MenuItemProvider;

@Component(immediate = true)
public class WikiApplication extends SkysailApplication implements MenuItemProvider, ApplicationProvider {

    private static final String APP_NAME = "Wiki";
    private DesignerRepository designerRepo;

    public WikiApplication() {
        super(APP_NAME);
        addToAppContext(ApplicationContextId.IMG, "/static/img/silk/page_link.png");
    }

    @Override
    protected void attach() {
        // Application root resource
        router.attach(new RouteBuilder("", RootResource.class));

        // router.attach(new RouteBuilder("/application/",
        // PostEntityResource.class));
        // router.attach(new RouteBuilder("/applications",
        // EntitiesResource.class));
        // router.attach(new RouteBuilder("/applications/{id}",
        // ContactResource.class));
        // router.attach(new RouteBuilder("/applications/{id}/",
        // PutContactResource.class));
    }

    @Reference(dynamic = true, multiple = false, optional = false, target = "(name=designerRepository)")
    public void setDesignerRepository(DbRepository repo) {
        this.designerRepo = (DesignerRepository) repo;
    }

    public void unsetDesignerRepository(DbRepository repo) {
        this.designerRepo = null;
    }

    public DesignerRepository getRepository() {
        return designerRepo;
    }

    public List<MenuItem> getMenuEntries() {
        MenuItem appMenu = new MenuItem("Appwiki", "/" + APP_NAME, this);
        appMenu.setCategory(MenuItem.Category.APPLICATION_MAIN_MENU);
        return Arrays.asList(appMenu);
    }

}