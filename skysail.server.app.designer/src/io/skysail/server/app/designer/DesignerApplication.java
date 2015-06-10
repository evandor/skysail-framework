package io.skysail.server.app.designer;

import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.designer.application.Application;
import io.skysail.server.app.designer.application.resources.ApplicationResource;
import io.skysail.server.app.designer.application.resources.ApplicationsResource;
import io.skysail.server.app.designer.application.resources.PostApplicationResource;
import io.skysail.server.app.designer.application.resources.PutApplicationResource;
import io.skysail.server.app.designer.codegen.PostCompilationResource;
import io.skysail.server.app.designer.codegen.SkysailApplicationCompiler;
import io.skysail.server.app.designer.codegen.SkysailEntityCompiler;
import io.skysail.server.app.designer.codegen.SkysailRepositoryCompiler;
import io.skysail.server.app.designer.entities.Entity;
import io.skysail.server.app.designer.entities.resources.EntitiesResource;
import io.skysail.server.app.designer.entities.resources.EntityResource;
import io.skysail.server.app.designer.entities.resources.PostEntityResource;
import io.skysail.server.app.designer.entities.resources.PostSubEntityResource;
import io.skysail.server.app.designer.entities.resources.PutEntityResource;
import io.skysail.server.app.designer.entities.resources.SubEntitiesResource;
import io.skysail.server.app.designer.entities.resources.SubEntityResource;
import io.skysail.server.app.designer.fields.EntityField;
import io.skysail.server.app.designer.fields.resources.FieldResource;
import io.skysail.server.app.designer.fields.resources.FieldsResource;
import io.skysail.server.app.designer.fields.resources.PostFieldResource;
import io.skysail.server.app.designer.fields.resources.PutFieldResource;
import io.skysail.server.app.designer.repo.DesignerRepository;
import io.skysail.server.db.DbRepository;
import io.skysail.server.db.DbService2;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.osgi.framework.BundleException;
import org.osgi.service.component.ComponentContext;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

import com.google.common.collect.Iterables;

import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.ApplicationContextId;
import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import de.twenty11.skysail.server.services.MenuItem;
import de.twenty11.skysail.server.services.MenuItemProvider;

@Component(immediate = true)
public class DesignerApplication extends SkysailApplication implements MenuItemProvider, ApplicationProvider {

    public static final String APP_NAME = "AppDesigner";
    public static final String ENTITY_ID = "entityId";
    public static final String FIELD_ID = "fieldId";

    private DesignerRepository repo;
    private DbService2 dbService;

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
        router.attach(new RouteBuilder("/applications/{id}/entities/{" + ENTITY_ID + "}", EntityResource.class));
        router.attach(new RouteBuilder("/applications/{id}/entities/{" + ENTITY_ID + "}/", PutEntityResource.class));

        router.attach(new RouteBuilder("/applications/{id}/entities/{" + ENTITY_ID + "}/onetomany",
                SubEntitiesResource.class));
        router.attach(new RouteBuilder("/applications/{id}/entities/{" + ENTITY_ID + "}/onetomany/{subEntityId}",
                SubEntityResource.class));

        router.attach(new RouteBuilder("/applications/{id}/entities/{" + ENTITY_ID + "}/onetomany/",
                PostSubEntityResource.class));

        router.attach(new RouteBuilder("/applications/{id}/entities/{" + ENTITY_ID + "}/fields", FieldsResource.class));
        router.attach(new RouteBuilder("/applications/{id}/entities/{" + ENTITY_ID + "}/fields/",
                PostFieldResource.class));
        router.attach(new RouteBuilder("/applications/{id}/entities/{" + ENTITY_ID + "}/fields/{" + FIELD_ID + "}",
                FieldResource.class));
        router.attach(new RouteBuilder("/applications/{id}/entities/{" + ENTITY_ID + "}/fields/{" + FIELD_ID + "}/",
                PutFieldResource.class));

        compileApplications();
    }

    public void compileApplications() {
        List<Application> apps = getRepository().findAll(Application.class);

        Map<String, String> routerPaths = new HashMap<>();
        List<String> entityNames = new ArrayList<>();
        List<String> entityClassNames = new ArrayList<>();

        apps.stream().forEach(
                a -> {
                    a.getEntities()
                            .stream()
                            .forEach(
                                    e -> {
                                        String entityName = getEntityName(a, e);
                                        SkysailEntityCompiler entityCompiler = new SkysailEntityCompiler(repo,
                                                getBundle(), a, entityName, e.getName());
                                        entityCompiler.createEntity(entityNames, entityClassNames);
                                        entityCompiler.createResources();
                                        entityCompiler.attachToRouter(router, a.getName(), e, routerPaths);
                                        
                                        handleSubEntities(a, e.getSubEntities(), entityNames, entityClassNames);
                                    });

                    SkysailRepositoryCompiler repoCompiler = new SkysailRepositoryCompiler(getBundle(), a);
                    repoCompiler.createRepository(entityNames, entityClassNames);

                    SkysailApplicationCompiler applicationCompiler = new SkysailApplicationCompiler(getBundle(), a,
                            routerPaths);
                    
                    applicationCompiler.createApplication();
                    
                    applicationCompiler.compile(getBundleContext());
                    if (applicationCompiler.isCompiledSuccessfully()) {                        
                        setupInMemoryBundle(applicationCompiler, repoCompiler);
                    }
                });

//        List<Entity> entities = apps.stream().map(a -> a.getEntities()).flatMap(e -> e.stream())
//                .collect(Collectors.toList());
//        handleSubEntries(entities);
    }

    private synchronized void setupInMemoryBundle(SkysailApplicationCompiler applicationCompiler,
            SkysailRepositoryCompiler repoCompiler) {
        Class<?> applicationClass = applicationCompiler.getApplicationClass();
        Class<?> repositoryClass = repoCompiler.getRepositoryClass();

        try {
            SkysailApplication applicationInstance = (SkysailApplication) applicationClass.newInstance();
            DbRepository dbRepoInstance = (DbRepository) repositoryClass.newInstance();
            System.out.println(applicationInstance);

            Method setDbServiceMethod = dbRepoInstance.getClass().getMethod("setDbService",
                    new Class[] { DbService2.class });
            setDbServiceMethod.invoke(dbRepoInstance, dbService);

            Method setRepositoryMethod = applicationInstance.getClass().getMethod("setRepository",
                    new Class[] { DbRepository.class });
            setRepositoryMethod.invoke(applicationInstance, dbRepoInstance);

            Method setComponentContextMethod = applicationInstance.getClass().getMethod("setComponentContext",
                    new Class[] { ComponentContext.class });
            setComponentContextMethod.invoke(applicationInstance, new Object[]{getComponentContext()});

            Method activateRepoInstance = dbRepoInstance.getClass().getMethod("activate", new Class[] {});
            activateRepoInstance.invoke(dbRepoInstance, new Object[] {});

            getBundleContext().registerService(
                    new String[] { ApplicationProvider.class.getName(), MenuItemProvider.class.getName() },
                    applicationInstance, null);

        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private String getEntityName(Application a, Entity e) {
        StringBuilder sb = new StringBuilder("AppDesigner");
        sb.append(a.getName());
        sb.append(Iterables.getLast(Arrays.asList(e.getName().split("\\."))));
        return e.getName();//sb.toString();
    }

    private void handleSubEntities(Application a, List<Entity> entities, List<String> entityNames, List<String> entityClassNames) {
        entities.forEach(sub -> {
            System.out.println(sub);
            
            String entityName = sub.getName();//getEntityName(a, e);
            SkysailEntityCompiler entityCompiler = new SkysailEntityCompiler(repo,
                    getBundle(), a, entityName, entityName);
            entityCompiler.createEntity(entityNames, entityClassNames);
            entityCompiler.createResources();
            //entityCompiler.attachToRouter(router, a.getName(), e, routerPaths);
            
            handleSubEntities(a, sub.getSubEntities(), entityNames, entityClassNames);
        });
    }

    @Reference(dynamic = true, multiple = false, optional = false, target = "(name=DesignerRepository)")
    public void setDesignerRepository(DbRepository repo) {
        this.repo = (DesignerRepository) repo;
    }

    public void unsetDesignerRepository(DbRepository repo) {
        this.repo = null;
    }

    @Reference(dynamic = true, multiple = false, optional = false)
    public void setDbService(DbService2 dbService) {
        this.dbService = dbService;
    }

    public void unsetDbService(DbRepository repo) {
        this.dbService = null;
    }

    public DesignerRepository getRepository() {
        return repo;
    }

    public List<MenuItem> getMenuEntries() {
        return super.getMenuEntriesWithCache();
    }

    public List<MenuItem> createMenuEntries() {
        MenuItem appMenu = new MenuItem("AppDesigner", "/" + APP_NAME, this);
        appMenu.setCategory(MenuItem.Category.APPLICATION_MAIN_MENU);
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(appMenu);
        menuItems.addAll(addDesignerAppMenuItems());
        return menuItems;
    }

    private List<MenuItem> addDesignerAppMenuItems() {
        List<Application> apps = getRepository().findAll(Application.class);
        return apps.stream().map(a -> {
            MenuItem menu = new MenuItem(a.getName(), "/" + APP_NAME + "/preview/" + a.getName(), this);
            menu.setCategory(MenuItem.Category.DESIGNER_APP_MENU);
            return menu;
        }).collect(Collectors.toList());
    }

    public Entity getEntity(Application application, String entityId) {
        for (Entity entity : application.getEntities()) {
            if (entity.getId().replace("#", "").equals(entityId)) {
                return entity;
            }
        }
        return null;
    }

    public Application getApplication(String id) {
        return getRepository().getById(Application.class, id);
    }

    public EntityField getEntityField(String appId, String entityId, String fieldId) {
        Application application = getRepository().getById(Application.class, appId);
        Optional<Entity> entityFromApplication = getEntityFromApplication(application, entityId);
        if (entityFromApplication.isPresent()) {
            List<EntityField> fields = entityFromApplication.get().getFields();
            return fields.stream().filter(f -> {
                if (f == null || f.getId() == null) {
                    return false;
                }
                return f.getId().replace("#", "").equals(fieldId);
            }).findFirst().orElse(null);
        }
        return null;
    }

    public Optional<Entity> getEntityFromApplication(Application application, String entityId) {
        return application.getEntities().stream().filter(e -> {
            if (e == null || e.getId() == null) {
                return false;
            }
            return e.getId().replace("#", "").equals(entityId);
        }).findFirst();
    }

    public void updateBundle() {
        Runnable command = new Runnable() {

            @Override
            public void run() {
                try {
                    getBundle().update();
                    // compileApplications();
                } catch (BundleException e) {
                    e.printStackTrace();
                }

            }
        };
        getTaskService().schedule(command, 1, TimeUnit.SECONDS);
    }

}