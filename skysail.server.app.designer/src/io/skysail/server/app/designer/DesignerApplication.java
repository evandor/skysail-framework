package io.skysail.server.app.designer;

import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.designer.application.Application;
import io.skysail.server.app.designer.application.resources.ApplicationResource;
import io.skysail.server.app.designer.application.resources.ApplicationsResource;
import io.skysail.server.app.designer.application.resources.PostApplicationResource;
import io.skysail.server.app.designer.application.resources.PutApplicationResource;
import io.skysail.server.app.designer.codegen.InMemoryJavaCompiler;
import io.skysail.server.app.designer.entities.Entity;
import io.skysail.server.app.designer.entities.resources.EntitiesResource;
import io.skysail.server.app.designer.entities.resources.EntityResource;
import io.skysail.server.app.designer.entities.resources.PostEntityResource;
import io.skysail.server.app.designer.entities.resources.PutEntityResource;
import io.skysail.server.app.designer.fields.EntityField;
import io.skysail.server.app.designer.fields.resources.FieldsResource;
import io.skysail.server.app.designer.fields.resources.PostFieldResource;
import io.skysail.server.app.designer.repo.DesignerRepository;
import io.skysail.server.db.DbRepository;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.server.restlet.resources.PostEntityServerResource;
import io.skysail.server.utils.BundleUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import lombok.extern.slf4j.Slf4j;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

import com.google.common.collect.Iterables;

import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.beans.DynamicEntity;
import de.twenty11.skysail.server.beans.EntityDynaProperty;
import de.twenty11.skysail.server.core.restlet.ApplicationContextId;
import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import de.twenty11.skysail.server.services.MenuItem;
import de.twenty11.skysail.server.services.MenuItemProvider;

@Component(immediate = true)
@Slf4j
public class DesignerApplication extends SkysailApplication implements MenuItemProvider, ApplicationProvider {

    public static final String APP_NAME = "AppDesigner";

    public static final String ENTITY_ID = "entityId";

    private DesignerRepository repo;

    private Map<String, Class<? extends DynamicEntity>> entityClasses = new HashMap<>();

    public DesignerApplication() {
        super(APP_NAME);
        addToAppContext(ApplicationContextId.IMG, "/static/img/silk/paintbrush.png");
    }

    @SuppressWarnings({ "unchecked" })
    @Override
    protected void attach() {
        // Application root resource
        router.attach(new RouteBuilder("", ApplicationsResource.class));

        router.attach(new RouteBuilder("/application/", PostApplicationResource.class));
        router.attach(new RouteBuilder("/applications", ApplicationsResource.class));
        router.attach(new RouteBuilder("/applications/{id}", ApplicationResource.class));
        router.attach(new RouteBuilder("/applications/{id}/", PutApplicationResource.class));

        router.attach(new RouteBuilder("/applications/{id}/entities", EntitiesResource.class));
        router.attach(new RouteBuilder("/applications/{id}/entities/", PostEntityResource.class));
        router.attach(new RouteBuilder("/applications/{id}/entities/{" + ENTITY_ID + "}", EntityResource.class));
        router.attach(new RouteBuilder("/applications/{id}/entities/{" + ENTITY_ID + "}/", PutEntityResource.class));

        router.attach(new RouteBuilder("/applications/{id}/entities/{" + ENTITY_ID + "}/fields", FieldsResource.class));
        router.attach(new RouteBuilder("/applications/{id}/entities/{" + ENTITY_ID + "}/fields/",
                PostFieldResource.class));

        String listServerResourceTemplate = BundleUtils.readResource(getBundle(), "code/ListServerResource.codegen");
        String postResourceTemplate = BundleUtils.readResource(getBundle(), "code/PostResource.codegen");
        String entityTemplate = BundleUtils.readResource(getBundle(), "code/Entity.codegen");

        List<Application> apps = getRepository().findAll(Application.class);
        apps.stream()
            .forEach(
                a -> {
                    a.getEntities()
                    .stream()
                    .forEach(
                            e -> {
                                StringBuilder sb = new StringBuilder("AppDesigner_");
                                sb.append(a.getName()).append("_");
                                sb.append(Iterables.getLast(Arrays.asList(e.getName().split("\\."))));
                                String entityName = sb.toString(); // e.g. AppDesigner_Automobiles_Brand

                                String entityClassName = setupEntityForCompilation(entityTemplate, a.getId(), entityName, e.getName()); // io.skysail.server.app.designer.codegen.AppDesigner_Automobiles_Brand
                                String postResourceClassName = setupPostResourceForCompilation(postResourceTemplate, entityName); 
                                String listResourceClassName = setupListResourceForCompilation(listServerResourceTemplate, a, entityName, entityClassName);

                                compile();

                                String path = "/preview/" + a.getName();

                                Class<? extends DynamicEntity> entityClass = (Class<? extends DynamicEntity>) getClass(entityClassName);
                                entityClasses.put(entityClassName, entityClass);
                                injectRepo(repo, entityClass);
                                getRepository().createWithSuperClass(DynamicEntity.class.getSimpleName(), entityName);
                                getRepository().register(entityClass);

                                Class<? extends PostEntityServerResource<?>> postResourceClass = (Class<? extends PostEntityServerResource<?>>) getClass(postResourceClassName);
                                router.attach(new RouteBuilder(path + "/" + e.getName() + "s/",
                                        postResourceClass));

                                Class<? extends ListServerResource<?>> listResourceClass = (Class<? extends ListServerResource<?>>) getClass(listResourceClassName);
                                router.attach(new RouteBuilder(path + "/" + e.getName() + "s",
                                        listResourceClass));
                                if (e.isRootEntity()) {
                                    router.attach(new RouteBuilder(path, listResourceClass));
                                }

                            });
                });
    }

    private String setupListResourceForCompilation(String listServerResourceTemplate, Application a, String entityName, String entityClassName) {
        final String theClassName = entityName + "sResource";
        @SuppressWarnings("serial")
        String listServerResourceCode = substitute(listServerResourceTemplate, new HashMap<String, String>() {
            {
                put("$classname$", theClassName);
                put("$entityname$", entityName);
                //put("$tablename$", entityClassName);//"dynamic." + a.getName() + "." + entityName);
            }
        });

        String className = "io.skysail.server.app.designer.codegen." + theClassName;
        collect(className, listServerResourceCode);
        return className;
    }

    private String setupPostResourceForCompilation(String postResourceTemplate, String entityName) {
        final String className2 = "Post" + entityName + "Resource";
        @SuppressWarnings("serial")
        String postResourceCode = substitute(postResourceTemplate, new HashMap<String, String>() {
            {
                put("$classname$", className2);
                put("$entityname$", entityName);
            }
        });
        String fullClassName = "io.skysail.server.app.designer.codegen." + className2;
        collect(fullClassName, postResourceCode);
        return fullClassName;
    }

    private String setupEntityForCompilation(String entityTemplate, String appId, String entityName, String appEntityName) {
        @SuppressWarnings("serial")
        String entityCode = substitute(entityTemplate, new HashMap<String, String>() {
            {
                put("$classname$", entityName);
                put("$applicationId$", appId);
                put("$appEntityName$", appEntityName);
            }
        });
        String entityClassName = "io.skysail.server.app.designer.codegen." + entityName;
        collect(entityClassName, entityCode);
        return entityClassName;
    }

    private void collect(String className, String entityCode) {
        try {
            InMemoryJavaCompiler.collect(className, entityCode);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        String filename = "./generated/" + className + ".java";
        try {
            Files.write(Paths.get(filename), entityCode.getBytes());
        } catch (IOException e) {
            log.debug("could not write source code for compilation unit '{}' to '{}'", className, filename);
        }

    }

    private void compile() {
        try {
            InMemoryJavaCompiler.compile(getBundleContext());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    private Class<?> getClass(String className) {
        try {
            return InMemoryJavaCompiler.getClass(className);
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    private String substitute(String template, Map<String, String> substitutionMap) {
        for (String key : substitutionMap.keySet()) {
            template = template.replace(key, substitutionMap.get(key));
        }
        return template;
    }

    @Reference(dynamic = true, multiple = false, optional = false, target = "(name=DesignerRepository)")
    public void setDesignerRepository(DbRepository repo) {
        this.repo = (DesignerRepository) repo;
        entityClasses.keySet().stream().forEach(key -> {
            injectRepo(repo, entityClasses.get(key));
        });
    }

    private void injectRepo(DbRepository repo, Class<? extends DynamicEntity> entityClass) {
        try {
            Method injectMethod = entityClass.getMethod("inject", new Class[] { DesignerRepository.class });
            injectMethod.invoke(entityClass, repo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void unsetDesignerRepository(DbRepository repo) {
        this.repo = null;
    }

    public DesignerRepository getRepository() {
        return repo;
    }

    public List<MenuItem> getMenuEntries() {
        List<MenuItem> result = new ArrayList<>();
        MenuItem appMenu = new MenuItem("AppDesigner", "/" + APP_NAME, this);
        appMenu.setCategory(MenuItem.Category.APPLICATION_MAIN_MENU);
        result.add(appMenu);
        addDesignerAppMenuItems(result);
        return result;
    }

    private void addDesignerAppMenuItems(List<MenuItem> result) {
        List<Application> apps = getRepository().findAll(Application.class);
        apps.stream().forEach(a -> {
            MenuItem menu = new MenuItem(a.getName(), "/" + APP_NAME + "/preview/" + a.getName(), this);
            menu.setCategory(MenuItem.Category.DESIGNER_APP_MENU);
            result.add(menu);
        });
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

    public static Set<EntityDynaProperty> getProperties(DesignerRepository repo, String beanName, String appIdentifier) {
        // super.getProperties
        SortedSet<EntityDynaProperty> properties = new TreeSet<>();

        Application designerApplication = repo.getById(Application.class, appIdentifier.replace("#", ""));
        List<Entity> entities = designerApplication.getEntities();

        // streams dont't seem to work here ?!?! (with orientdb objects)
        for (Entity entity : entities) {
            if (beanName.equals(entity.getName())) {
                List<EntityField> fields = entity.getFields();
                for (EntityField entityField : fields) {
                    properties.add(new EntityDynaProperty(entityField.getName(), String.class));
                }
                break;
            }
        }

        return properties;
    }

}