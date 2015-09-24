package io.skysail.server.app.wiki;

import io.skysail.api.text.TranslationRenderService;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.designer.repo.DesignerRepository;
import io.skysail.server.app.wiki.pages.resources.*;
import io.skysail.server.app.wiki.repository.*;
import io.skysail.server.app.wiki.spaces.resources.*;
import io.skysail.server.menus.*;
import io.skysail.server.repo.DbRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import lombok.Getter;
import aQute.bnd.annotation.component.*;
import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.*;
import de.twenty11.skysail.server.services.*;

@Component(immediate = true)
public class WikiApplication extends SkysailApplication implements MenuItemProvider, ApplicationProvider {

    public static final String APP_NAME = "Wiki";

    private DesignerRepository designerRepo;

    private List<AtomicReference<TranslationRenderService>> rendererServices = new ArrayList<>();

    @Getter
    private SpacesRepository spacesRepo;

    @Getter
    private PagesRepository pagesRepo;

    public WikiApplication() {
        super(APP_NAME);
        addToAppContext(ApplicationContextId.IMG, "/static/img/silk/page_link.png");
    }

    @Override
    protected void attach() {
        super.attach();

        // Application root resource
        router.attach(new RouteBuilder("", SpacesResource.class));
        router.attach(new RouteBuilder("/", SpacesResource.class));
        router.attach(new RouteBuilder("/spaces", SpacesResource.class).noAuthenticationNeeded());
        router.attach(new RouteBuilder("/spaces/", PostSpaceResource.class));
        router.attach(new RouteBuilder("/spaces/{id}", SpaceResource.class));
        router.attach(new RouteBuilder("/spaces/{id}/", PutSpaceResource.class));

        router.attach(new RouteBuilder("/spaces/{id}/pages", PagesResource.class));
        router.attach(new RouteBuilder("/spaces/{id}/pages/", PostPageResource.class));
        // router.attach(new RouteBuilder("/spaces/{id}/pages/{pageId}",
        // PageResource.class).noAuthenticationNeeded());
        router.attach(new RouteBuilder("/spaces/{id}/pages/{pageId}/", PutPageResource.class));

        router.attach(new RouteBuilder("/pages/{pageId}", PageResource.class).noAuthenticationNeeded());
        router.attach(new RouteBuilder("/pages/{pageId}/pages", SubpagesResource.class));
        router.attach(new RouteBuilder("/pages/{pageId}/pages/", PostSubPageResource.class));
        router.attach(new RouteBuilder("/pages/{pageId}/", PutPageResource.class));

        router.attach(new RouteBuilder("/_public/spaces", PublicSpacesResource.class).noAuthenticationNeeded());
        router.attach(new RouteBuilder("/preview/pages/{pageId}", PublicPageResource.class));

    }

    @Reference(dynamic = true, multiple = false, optional = false, target = "(name=PagesRepository)")
    public void setPagesRepository(DbRepository repo) {
        this.pagesRepo = (PagesRepository) repo;
    }

    public void unsetPagesRepository(DbRepository repo) {
        this.pagesRepo = null;
    }

    @Reference(dynamic = true, multiple = false, optional = false, target = "(name=SpacesRepository)")
    public void setSpacesRepository(DbRepository repo) {
        this.spacesRepo = (SpacesRepository) repo;
    }

    public void unsetSpacesRepository(DbRepository repo) {
        this.spacesRepo = null;
    }

    @Reference(dynamic = true, multiple = true, optional = false)
    public void addTranslationRenderService(TranslationRenderService service) {
        rendererServices.add(new AtomicReference<TranslationRenderService>(service));
    }

    public void removeTranslationRenderService(TranslationRenderService service) {
        rendererServices.remove(new AtomicReference<TranslationRenderService>(service));
    }

    public TranslationRenderService getMarkdownRenderer() {
        return rendererServices.stream().map(AtomicReference::get).filter(service -> {
            return service.getClass().toString().contains("Markdown");
        }).findFirst().orElseThrow(IllegalStateException::new);
    }

    public List<MenuItem> getMenuEntries() {
        MenuItem appMenu = new MenuItem("Appwiki", "/" + APP_NAME + getApiVersion().getVersionPath(), this);
        appMenu.setCategory(MenuItem.Category.APPLICATION_MAIN_MENU);
        return Arrays.asList(appMenu);
    }

}