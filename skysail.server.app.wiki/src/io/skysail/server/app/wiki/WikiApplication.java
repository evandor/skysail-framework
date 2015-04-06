package io.skysail.server.app.wiki;

import io.skysail.server.app.designer.repo.DesignerRepository;
import io.skysail.server.app.wiki.spaces.PostSpaceResource;
import io.skysail.server.app.wiki.spaces.Space;
import io.skysail.server.app.wiki.spaces.SpacesResource;
import io.skysail.server.db.DbRepository;

import java.util.Arrays;
import java.util.List;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.app.SkysailApplication;
import de.twenty11.skysail.server.core.restlet.ApplicationContextId;
import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import de.twenty11.skysail.server.services.MenuItem;
import de.twenty11.skysail.server.services.MenuItemProvider;

@Component(immediate = true)
public class WikiApplication extends SkysailApplication implements MenuItemProvider, ApplicationProvider {

    private static final String APP_NAME = "Wiki";
    private DesignerRepository designerRepo;
    private WikiRepository wikiRepo;

    public WikiApplication() {
        super(APP_NAME);
        addToAppContext(ApplicationContextId.IMG, "/static/img/silk/page_link.png");
    }

    @Override
    protected void attach() {
        // Application root resource
        router.attach(new RouteBuilder("", RootResource.class));
        router.attach(new RouteBuilder("/", RootResource.class));
        router.attach(new RouteBuilder("/spaces", SpacesResource.class));
        router.attach(new RouteBuilder("/spaces/", PostSpaceResource.class));

        // router.attach(new RouteBuilder("/application/",
        // PostEntityResource.class));
        // router.attach(new RouteBuilder("/applications",
        // EntitiesResource.class));
        // router.attach(new RouteBuilder("/applications/{id}",
        // ContactResource.class));
        // router.attach(new RouteBuilder("/applications/{id}/",
        // PutContactResource.class));
    }

    @Reference(dynamic = true, multiple = true, optional = false)
    public void setDesignerRepository(DbRepository repo) {
        if (repo instanceof DesignerRepository) {
            this.designerRepo = (DesignerRepository) repo;
            Space.inject(designerRepo);
        }
    }

    public void unsetDesignerRepository(DbRepository repo) {
        this.designerRepo = null;
        Space.inject(null);
    }

    @Reference(dynamic = true, multiple = false, optional = false, target = "(name=wikiRepository)")
    public void setWikiRepository(DbRepository repo) {
        this.wikiRepo = (WikiRepository) repo;
    }

    public void unsetWikiRepository(DbRepository repo) {
        this.wikiRepo = null;
    }

    public WikiRepository getRepository() {
        return wikiRepo;
    }

    public List<MenuItem> getMenuEntries() {
        MenuItem appMenu = new MenuItem("Appwiki", "/" + APP_NAME, this);
        appMenu.setCategory(MenuItem.Category.APPLICATION_MAIN_MENU);
        return Arrays.asList(appMenu);
    }

}