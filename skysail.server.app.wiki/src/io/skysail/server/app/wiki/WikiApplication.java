package io.skysail.server.app.wiki;

import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.designer.repo.DesignerRepository;
import io.skysail.server.app.wiki.pages.Page;
import io.skysail.server.app.wiki.pages.resources.*;
import io.skysail.server.app.wiki.repository.WikiRepository;
import io.skysail.server.app.wiki.spaces.resources.*;
import io.skysail.server.db.DbRepository;

import java.util.*;

import aQute.bnd.annotation.component.*;
import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.*;
import de.twenty11.skysail.server.services.*;

@Component(immediate = true)
public class WikiApplication extends SkysailApplication implements MenuItemProvider, ApplicationProvider {

    public static final String APP_NAME = "Wiki";
    
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
        router.attach(new RouteBuilder("/spaces/{id}", SpaceResource.class));
        router.attach(new RouteBuilder("/spaces/{id}/", PutSpaceResource.class));

        router.attach(new RouteBuilder("/spaces/{id}/pages", PagesResource.class));
        router.attach(new RouteBuilder("/spaces/{id}/pages/", PostPageResource.class));
        router.attach(new RouteBuilder("/spaces/{id}/pages/{pageId}", PageResource.class));
        router.attach(new RouteBuilder("/spaces/{id}/pages/{pageId}/", PutPageResource.class));

    }

    @Reference(dynamic = true, multiple = true, optional = false)
    public void setDesignerRepository(DbRepository repo) {
        if (repo instanceof DesignerRepository) {
            this.designerRepo = (DesignerRepository) repo;
            //Space.inject(designerRepo);
            Page.inject(designerRepo);
        }
    }

    public void unsetDesignerRepository(DbRepository repo) {
        this.designerRepo = null;
        //Space.inject(null);
        Page.inject(null);
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