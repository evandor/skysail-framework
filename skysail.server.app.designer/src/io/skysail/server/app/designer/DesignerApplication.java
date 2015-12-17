package io.skysail.server.app.designer;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.osgi.framework.BundleException;
import org.osgi.service.component.annotations.*;
import org.osgi.service.event.EventAdmin;

import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.*;
import io.skysail.api.repos.DbRepository;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.application.resources.*;
import io.skysail.server.app.designer.codegen.PostCompilationResource;
import io.skysail.server.app.designer.entities.DbEntity;
import io.skysail.server.app.designer.entities.resources.*;
import io.skysail.server.app.designer.fields.DbEntityField;
import io.skysail.server.app.designer.fields.resources.*;
import io.skysail.server.app.designer.repo.DesignerRepository;
import io.skysail.server.db.DbService;
import io.skysail.domain.core.Repositories;
import io.skysail.server.menus.*;
import lombok.Getter;

@Component(immediate = true)
public class DesignerApplication extends SkysailApplication implements MenuItemProvider, ApplicationProvider {

    public static final String APP_NAME = "AppDesigner";
    public static final String ENTITY_ID = "eid";
    public static final String FIELD_ID = "fieldId";

    private DesignerRepository repo;
    private DbService dbService;
    
    @org.osgi.service.component.annotations.Reference(cardinality = ReferenceCardinality.OPTIONAL)
    @Getter
    private volatile EventAdmin eventAdmin;
    private Repositories repos;

    public DesignerApplication() {
        super(APP_NAME);
        addToAppContext(ApplicationContextId.IMG, "/static/img/silk/paintbrush.png");
    }
    
    @Override
    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY, unbind = "unsetRepositories")
    public void setRepositories(Repositories repos) {
       this.repos = repos;
    }

    public void unsetRepositories(Repositories repo) {
        this.repos = null;
    }


    @Override
    protected void attach() {
        super.attach();
        router.attach(new RouteBuilder("", ApplicationsResource.class));

        router.attach(new RouteBuilder("/application/", PostApplicationResource.class));
        router.attach(new RouteBuilder("/applications", ApplicationsResource.class));
        router.attach(new RouteBuilder("/applications/{id}", ApplicationResource.class));
        router.attach(new RouteBuilder("/applications/{id}/", PutApplicationResource.class));

        router.attach(new RouteBuilder("/applications/{id}/compilations/", PostCompilationResource.class));

        router.attach(new RouteBuilder("/applications/{id}/entities", EntitiesResource.class));
        router.attach(new RouteBuilder("/applications/{id}/entities/", PostEntityResource.class));
        router.attach(new RouteBuilder("/applications/{id}/entities/{eid}", EntityResource.class));
        router.attach(new RouteBuilder("/applications/{id}/entities/{eid}/", PutEntityResource.class));

        router.attach(new RouteBuilder("/entities/{" + ENTITY_ID + "}/onetomany", SubEntitiesResource.class));
        router.attach(new RouteBuilder("/entities/{" + ENTITY_ID + "}/onetomany/{subEntityId}", SubEntityResource.class));

        router.attach(new RouteBuilder("/entities/{" + ENTITY_ID + "}/onetomany/", PostSubEntityResource.class));

        router.attach(new RouteBuilder("/entities/{" + ENTITY_ID + "}/fields", FieldsResource.class));
        router.attach(new RouteBuilder("/entities/{" + ENTITY_ID + "}/fields/", PostFieldResource.class));
        router.attach(new RouteBuilder("/entities/{" + ENTITY_ID + "}/fields/{" + FIELD_ID + "}", FieldResource.class));
        router.attach(new RouteBuilder("/entities/{" + ENTITY_ID + "}/fields/{" + FIELD_ID + "}/",PutFieldResource.class));

        router.attach(new RouteBuilder("/entities/{" + ENTITY_ID + "}/actionfields/", PostActionFieldResource.class));
        
        
        router.attach(new RouteBuilder("/import/", ImportResource.class));
    }

    @org.osgi.service.component.annotations.Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY, target = "(name=DesignerRepository)")
    public void setDesignerRepository(DbRepository repo) {
        this.repo = (DesignerRepository) repo;
    }

    public void unsetDesignerRepository(DbRepository repo) {
        this.repo = null;
    }

    @org.osgi.service.component.annotations.Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY)
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

    public void compileApplication(String appId) {
        getRepository().findAll(DbApplication.class).stream().filter(app -> app.getId().equals("#"+appId)).findFirst().ifPresent(app -> {
            ApplicationCreator applicationCreator = new ApplicationCreator(app, repo, repos, getBundle());
            applicationCreator.createApplication(dbService, getComponentContext());
        });
    }

    private List<MenuItem> addDesignerAppMenuItems() {
        List<DbApplication> apps = getRepository().findAll(DbApplication.class);
        return apps.stream().map(a -> {
            MenuItem menu = new MenuItem(a.getName(), "/" + APP_NAME + "/preview/" + a.getName(), this);
            menu.setCategory(MenuItem.Category.DESIGNER_APP_MENU);
            return menu;
        }).collect(Collectors.toList());
    }

    public DbEntity getEntity(String entityId) {
        return getRepository().getById(DbEntity.class, entityId);
    }

    public DbApplication getApplication(String id) {
        return getRepository().getById(DbApplication.class, id);
    }

    public DbEntityField getEntityField(String appId, String entityId, String fieldId) {
        DbApplication application = getRepository().getById(DbApplication.class, appId);
        Optional<DbEntity> entityFromApplication = null;//getEntityFromApplication(application, entityId);
        if (entityFromApplication.isPresent()) {
            List<DbEntityField> fields = Collections.emptyList();//entityFromApplication.get().getFields();
            return fields.stream().filter(f -> {
                if (f == null || f.getId() == null) {
                    return false;
                }
                return f.getId().replace("#", "").equals(fieldId);
            }).findFirst().orElse(null);
        }
        return null;
    }

    public Optional<String> getEntityFromApplication(DbApplication application, String entityId) {
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