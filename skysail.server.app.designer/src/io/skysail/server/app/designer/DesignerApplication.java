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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import lombok.extern.slf4j.Slf4j;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import de.twenty11.skysail.server.app.ApplicationProvider;
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

    public DesignerApplication() {
        super(APP_NAME);
        addToAppContext(ApplicationContextId.IMG, "/static/img/silk/paintbrush.png");
    }

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
        
//        URL url = getBundle().getResource("code/ListServerResource.codegen");
//        BufferedReader br;
//        try {
//            br = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
//            while(br.ready()){
//                System.out.println(br.readLine());
//            }
//            br.close();
//        } catch (IOException e2) {
//            e2.printStackTrace();
//        }

        List<Application> apps = getRepository().findAll(Application.class);
        apps.stream().forEach( a -> { 

            a.getEntities().stream().forEach(e -> {
                System.out.println(a);
                String entityName = e.getName();
                String className = entityName.substring(0,1).toUpperCase().concat(entityName.substring(1)).concat("sResource");
                String path = "/preview/" + a.getName();// + "/"  + entityName;
    
                StringBuffer sourceCode = new StringBuffer();
                sourceCode.append("package io.skysail.server.app.designer.codegen;\n");
                sourceCode.append("\n");
                sourceCode.append("import io.skysail.server.restlet.resources.ListServerResource;\n");
                sourceCode.append("import io.skysail.server.app.designer.DesignerApplication;\n");
                sourceCode.append("\n");
                sourceCode.append("import java.util.List;\n");
                sourceCode.append("import java.util.Map;\n");
                sourceCode.append("\n");
                sourceCode.append("public class "+className+" extends ListServerResource<Map<String,Object>> {\n");
                sourceCode.append("   public List<Map<String,Object>> getEntity() {\n");
                sourceCode.append("       return ((DesignerApplication) getApplication()).getRepository().findAll(\"select from dynamic."+a.getName()+"."+entityName+"\");\n");
                sourceCode.append("   }\n");
                sourceCode.append("   public List<Link> getLinks() {\n");
                sourceCode.append("       return super.getLinks(PostSpaceResource.class);\n");
                sourceCode.append("   }\n");
                sourceCode.append("}");
    
                try {
                    Class<?> compiledClass = InMemoryJavaCompiler.compile(getBundleContext(), "io.skysail.server.app.designer.codegen." + className, sourceCode.toString());
                    Class<? extends ListServerResource<?>> helloClass = (Class<? extends ListServerResource<?>>) compiledClass;
                    router.attach(new RouteBuilder(path, helloClass));
                } catch (Exception e1) {
                   log.error(e1.getMessage(),e1);
                }
            });
        });
        

    }

    @Reference(dynamic = true, multiple = false, optional = false, target = "(name=DesignerRepository)")
    public void setDesignerRepository(DbRepository repo) {
        this.repo = (DesignerRepository) repo;
    }

    public void unsetDesignerRepository(DbRepository repo) {
        this.repo = null;
    }

    public DesignerRepository getRepository() {
        return repo;
    }

    public List<MenuItem> getMenuEntries() {
        MenuItem appMenu = new MenuItem("AppDesigner", "/" + APP_NAME, this);
        appMenu.setCategory(MenuItem.Category.APPLICATION_MAIN_MENU);

        List<MenuItem> result = new ArrayList<>();
        result.add(appMenu);

        List<Application> apps = getRepository().findAll(Application.class);
        apps.stream().forEach(a -> {
            MenuItem menu = new MenuItem(a.getName(), "/" + APP_NAME + "/preview/" + a.getName(), this);
            menu.setCategory(MenuItem.Category.APPLICATION_MAIN_MENU);
            result.add(menu);
        });
        return result;
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

        Application designerApplication = repo.getById(Application.class, appIdentifier);
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