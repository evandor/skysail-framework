package io.skysail.server.app.designer;

import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.designer.application.Application;
import io.skysail.server.app.designer.application.resources.*;
import io.skysail.server.app.designer.codegen.PostCompilationResource;
import io.skysail.server.app.designer.entities.Entity;
import io.skysail.server.app.designer.entities.resources.*;
import io.skysail.server.app.designer.fields.EntityField;
import io.skysail.server.app.designer.fields.resources.*;
import io.skysail.server.app.designer.repo.DesignerRepository;
import io.skysail.server.db.DbService;
import io.skysail.server.menus.*;
import io.skysail.server.repo.DbRepository;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import org.osgi.framework.BundleException;

import aQute.bnd.annotation.component.*;
import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.*;
import de.twenty11.skysail.server.services.*;

@Component(immediate = true)
@Slf4j
public class DesignerApplication extends SkysailApplication implements MenuItemProvider, ApplicationProvider {

    public static final String APP_NAME = "AppDesigner";
    public static final String ENTITY_ID = "id";
    public static final String FIELD_ID = "fieldId";

    private DesignerRepository repo;
    private DbService dbService;

    public DesignerApplication() {
        super(APP_NAME);
        addToAppContext(ApplicationContextId.IMG, "/static/img/silk/paintbrush.png");
    }

    @Override
    protected void attach() {
        super.attach();
        router.attach(new RouteBuilder("", ApplicationsResource.class));

        router.attach(new RouteBuilder("/compilations/", PostCompilationResource.class));

        router.attach(new RouteBuilder("/application/", PostApplicationResource.class));
        router.attach(new RouteBuilder("/applications", ApplicationsResource.class));
        router.attach(new RouteBuilder("/applications/{id}", ApplicationResource.class));
        router.attach(new RouteBuilder("/applications/{id}/", PutApplicationResource.class));

        router.attach(new RouteBuilder("/applications/{id}/entities", EntitiesResource.class));
        router.attach(new RouteBuilder("/applications/{id}/entities/", PostEntityResource.class));
        router.attach(new RouteBuilder("/entities/{" + ENTITY_ID + "}", EntityResource.class));
        router.attach(new RouteBuilder("/entities/{" + ENTITY_ID + "}/", PutEntityResource.class));

        router.attach(new RouteBuilder("/entities/{" + ENTITY_ID + "}/onetomany", SubEntitiesResource.class));
        router.attach(new RouteBuilder("/entities/{" + ENTITY_ID + "}/onetomany/{subEntityId}", SubEntityResource.class));

        router.attach(new RouteBuilder("/entities/{" + ENTITY_ID + "}/onetomany/", PostSubEntityResource.class));

        router.attach(new RouteBuilder("/entities/{" + ENTITY_ID + "}/fields", FieldsResource.class));
        router.attach(new RouteBuilder("/entities/{" + ENTITY_ID + "}/fields/", PostFieldResource.class));
        router.attach(new RouteBuilder("/entities/{" + ENTITY_ID + "}/fields/{" + FIELD_ID + "}", FieldResource.class));
        router.attach(new RouteBuilder("/entities/{" + ENTITY_ID + "}/fields/{" + FIELD_ID + "}/",PutFieldResource.class));

        router.attach(new RouteBuilder("/entities/{" + ENTITY_ID + "}/actionfields/", PostActionFieldResource.class));

        compileApplications();
    }

    @Reference(dynamic = true, multiple = false, optional = false, target = "(name=DesignerRepository)")
    public void setDesignerRepository(DbRepository repo) {
        this.repo = (DesignerRepository) repo;
    }

    public void unsetDesignerRepository(DbRepository repo) {
        this.repo = null;
    }

    @Reference(dynamic = true, multiple = false, optional = false)
    public void setDbService(DbService dbService) {
        this.dbService = dbService;
    }

    public void unsetDbService(DbService dbService) {
        this.dbService = null;
    }

    public DesignerRepository getRepository() {
        return repo;
    }

    public List<MenuItem> getMenuEntries() {
        return super.getMenuEntriesWithCache();
    }

    public List<MenuItem> createMenuEntries() {
        MenuItem appMenu = new MenuItem("AppDesigner", "/" + APP_NAME + getApiVersion().getVersionPath(), this);
        appMenu.setCategory(MenuItem.Category.APPLICATION_MAIN_MENU);
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(appMenu);
        menuItems.addAll(addDesignerAppMenuItems());
        return menuItems;
    }

    public void compileApplications() {
        getRepository().findAll(Application.class).stream().forEach(app -> {
            try {
                ApplicationCreator applicationCreator = new ApplicationCreator(app, router, repo, getBundle());
                if (applicationCreator.create(getEventAdmin())) {
                    applicationCreator.setupInMemoryBundle(dbService, getComponentContext());
                }
            } catch (IllegalStateException ise) {
                log.error(ise.getMessage(), ise);
            }
        });
    }

    private List<MenuItem> addDesignerAppMenuItems() {
        List<Application> apps = getRepository().findAll(Application.class);
        return apps.stream().map(a -> {
            MenuItem menu = new MenuItem(a.getName(), "/" + APP_NAME + "/preview/" + a.getName(), this);
            menu.setCategory(MenuItem.Category.DESIGNER_APP_MENU);
            return menu;
        }).collect(Collectors.toList());
    }

    public Entity getEntity(String entityId) {
        return getRepository().getById(Entity.class, entityId);
    }

    public Application getApplication(String id) {
        return getRepository().getById(Application.class, id);
    }

    public EntityField getEntityField(String appId, String entityId, String fieldId) {
        Application application = getRepository().getById(Application.class, appId);
        Optional<Entity> entityFromApplication = null;//getEntityFromApplication(application, entityId);
        if (entityFromApplication.isPresent()) {
            List<EntityField> fields = Collections.emptyList();//entityFromApplication.get().getFields();
            return fields.stream().filter(f -> {
                if (f == null || f.getId() == null) {
                    return false;
                }
                return f.getId().replace("#", "").equals(fieldId);
            }).findFirst().orElse(null);
        }
        return null;
    }

    public Optional<String> getEntityFromApplication(Application application, String entityId) {
//        return application.getEntities().stream().filter(e -> {
////            if (e == null || e.getId() == null) {
////                return false;
////            }
////            return e.getId().replace("#", "").equals(entityId);
//        }).findFirst();
        return null;
    }

    public void updateBundle() {
        Runnable command = new Runnable() {

            @Override
            public void run() {
                try {
                    getBundle().update();
                } catch (BundleException e) {
                    e.printStackTrace();
                }

            }
        };
        getTaskService().schedule(command, 1, TimeUnit.SECONDS);
    }

}