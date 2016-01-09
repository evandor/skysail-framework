package io.skysail.server.app.designer;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.event.EventAdmin;

import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.ApplicationContextId;
import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import io.skysail.domain.core.Repositories;
import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.application.resources.*;
import io.skysail.server.app.designer.codegen.PostCompilationResource;
import io.skysail.server.app.designer.entities.DbEntity;
import io.skysail.server.app.designer.entities.resources.EntitiesResource;
import io.skysail.server.app.designer.entities.resources.EntityResource;
import io.skysail.server.app.designer.entities.resources.PostEntityResource;
import io.skysail.server.app.designer.entities.resources.PutEntityResource;
import io.skysail.server.app.designer.fields.resources.FieldResource;
import io.skysail.server.app.designer.fields.resources.FieldsResource;
import io.skysail.server.app.designer.fields.resources.PutFieldRedirectResource;
import io.skysail.server.app.designer.fields.resources.date.PostDateFieldResource;
import io.skysail.server.app.designer.fields.resources.date.PutDateFieldResource;
import io.skysail.server.app.designer.fields.resources.editors.PostTrixeditorFieldResource;
import io.skysail.server.app.designer.fields.resources.editors.PutTrixeditorFieldResource;
import io.skysail.server.app.designer.fields.resources.text.PostTextFieldResource;
import io.skysail.server.app.designer.fields.resources.text.PutTextFieldResource;
import io.skysail.server.app.designer.fields.resources.textarea.PostTextareaFieldResource;
import io.skysail.server.app.designer.fields.resources.textarea.PutTextareaFieldResource;
import io.skysail.server.app.designer.relations.resources.PostRelationResource;
import io.skysail.server.app.designer.relations.resources.RelationResource;
import io.skysail.server.app.designer.relations.resources.RelationsResource;
import io.skysail.server.app.designer.repo.DesignerRepository;
import io.skysail.server.db.DbService;
import io.skysail.server.menus.MenuItem;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.restlet.resources.TreeRepresentation;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Component(immediate = true)
@Slf4j
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
        router.attach(new RouteBuilder("/update/", UpdateBundleResource.class));

        router.attach(new RouteBuilder("/applications/{id}/entities", EntitiesResource.class));
        router.attach(new RouteBuilder("/applications/{id}/entities/", PostEntityResource.class));
        router.attach(new RouteBuilder("/applications/{id}/entities/{eid}", EntityResource.class));
        router.attach(new RouteBuilder("/applications/{id}/entities/{eid}/", PutEntityResource.class));

//        router.attach(new RouteBuilder("/entities/{" + ENTITY_ID + "}/onetomany", SubEntitiesResource.class)); // NOSONAR
//        router.attach(new RouteBuilder("/entities/{" + ENTITY_ID + "}/onetomany/{subEntityId}", SubEntityResource.class));
//
//        router.attach(new RouteBuilder("/entities/{" + ENTITY_ID + "}/onetomany/", PostSubEntityResource.class));

        router.attach(new RouteBuilder("/applications/{id}/entities/{" + ENTITY_ID + "}/fields", FieldsResource.class));
        
        router.attach(new RouteBuilder("/entities/{" + ENTITY_ID + "}/datefields/", PostDateFieldResource.class));
        router.attach(new RouteBuilder("/entities/{" + ENTITY_ID + "}/datefields/{"+FIELD_ID+"}/", PutDateFieldResource.class));

        router.attach(new RouteBuilder("/entities/{" + ENTITY_ID + "}/textfields/", PostTextFieldResource.class));
        router.attach(new RouteBuilder("/entities/{" + ENTITY_ID + "}/textfields/{"+FIELD_ID+"}/", PutTextFieldResource.class));

        router.attach(new RouteBuilder("/entities/{" + ENTITY_ID + "}/textareafields/", PostTextareaFieldResource.class));
        router.attach(new RouteBuilder("/entities/{" + ENTITY_ID + "}/textareafields/{"+FIELD_ID+"}/", PutTextareaFieldResource.class));

        router.attach(new RouteBuilder("/entities/{" + ENTITY_ID + "}/trixeditor/", PostTrixeditorFieldResource.class));
        router.attach(new RouteBuilder("/entities/{" + ENTITY_ID + "}/trixeditor/{"+FIELD_ID+"}/", PutTrixeditorFieldResource.class));

        router.attach(new RouteBuilder("/entities/{" + ENTITY_ID + "}/relations", RelationsResource.class));
        router.attach(new RouteBuilder("/entities/{" + ENTITY_ID + "}/relations/", PostRelationResource.class));
        router.attach(new RouteBuilder("/entities/{" + ENTITY_ID + "}/relations/{id}", RelationResource.class));


        router.attach(new RouteBuilder("/entities/{" + ENTITY_ID + "}/fields/{" + FIELD_ID + "}", FieldResource.class));
        
        router.attach(new RouteBuilder("/entities/{" + ENTITY_ID + "}/fields/{" + FIELD_ID + "}/", PutFieldRedirectResource.class));
        
        router.attach(new RouteBuilder("/import/", ImportResource.class).authorizeWith(anyOf("admin")));
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
        return apps.stream()
            .filter(a -> a != null)
            .map(a -> {
                MenuItem menu = new MenuItem(a.getName(), "/" + APP_NAME + "/preview/" + a.getName(), this);
                menu.setCategory(MenuItem.Category.DESIGNER_APP_MENU);
                return menu;
            })
            .collect(Collectors.toList());
    }

    public DbEntity getEntity(String entityId) {
        return getRepository().getById(DbEntity.class, entityId);
    }

    public DbApplication getApplication(String id) {
        return getRepository().getById(DbApplication.class, id);
    }

    public void updateBundle() {
        Runnable command = new Runnable() {

            @Override
            public void run() {
                try {
                    Bundle bundle = getBundle();
                    log.info("about to update bundle {} [{}]", bundle.getSymbolicName(), bundle.getVersion().toString());
                    bundle.update();
                    log.info("successfully updated bundle {} [{}]", bundle.getSymbolicName(), bundle.getVersion().toString());
                } catch (BundleException e) {
                    log.error(e.getMessage(), e);
                }

            }
        };
        getTaskService().schedule(command, 1, TimeUnit.SECONDS);
    }

    public List<TreeRepresentation> getTreeRepresentation(DbApplication dbApplication) {
        if (dbApplication != null) {
            return Arrays.asList(new TreeRepresentation(dbApplication,""));
        }
        return Collections.emptyList();
    }

    public List<TreeRepresentation> getTreeRepresentation(String appId) {
        DbApplication dbApplication = getApplication(appId);
        if (dbApplication != null) {
            return Arrays.asList(new TreeRepresentation(dbApplication,""));
        }
        return Collections.emptyList();
    }

}