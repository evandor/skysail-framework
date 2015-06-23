package io.skysail.server.app.designer;

import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.designer.application.Application;
import io.skysail.server.app.designer.codegen.InMemoryJavaCompiler;
import io.skysail.server.app.designer.codegen.SkysailApplicationCompiler;
import io.skysail.server.app.designer.codegen.SkysailEntityCompiler;
import io.skysail.server.app.designer.codegen.SkysailRepositoryCompiler;
import io.skysail.server.app.designer.codegen.SkysailSubEntityCompiler;
import io.skysail.server.app.designer.entities.Entity;
import io.skysail.server.app.designer.model.ApplicationModel;
import io.skysail.server.app.designer.model.EntityModel;
import io.skysail.server.app.designer.repo.DesignerRepository;
import io.skysail.server.db.DbRepository;
import io.skysail.server.db.DbService2;
import io.skysail.server.utils.BundleUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import lombok.Getter;

import org.osgi.framework.Bundle;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.event.EventAdmin;
import org.stringtemplate.v4.ST;

import com.google.common.collect.Iterables;

import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.osgi.EventHelper;
import de.twenty11.skysail.server.core.restlet.SkysailRouter;
import de.twenty11.skysail.server.services.MenuItemProvider;

public class ApplicationCreator {

    private Application application;
    private Bundle bundle;
    private Map<String, String> routerPaths = new HashMap<>();
    private SkysailRouter router;
    private DesignerRepository repo;
    private SkysailRepositoryCompiler repoCompiler;
    private SkysailApplicationCompiler applicationCompiler;

    private List<String> entityNames = new ArrayList<>();
    private List<String> entityClassNames = new ArrayList<>();

    @Getter
    private ApplicationModel applicationModel;

   // private Map<String, String> templates;
    private ST stringTemplateRoot;

    public ApplicationCreator(Application application, SkysailRouter router, DesignerRepository repo, Bundle bundle) {
        this.application = application;
        this.router = router;
        this.repo = repo;
        this.bundle = bundle;
        this.applicationModel = new ApplicationModel(application, repo);
        stringTemplateRoot = getStringTemplateIndex(new STGroupBundleDir(bundle, "/code2"), applicationModel);
    }

    private ST getStringTemplateIndex(STGroupBundleDir stGroup, ApplicationModel applicationModel) {
        ST javafile = stGroup.getInstanceOf("javafile");
        javafile.add("application", applicationModel);
        return javafile;
    }

    public boolean create(AtomicReference<EventAdmin> eventAdminRef) {

        InMemoryJavaCompiler.reset();

        new EntityCreator(applicationModel).create(stringTemplateRoot);
        
        entityClassNames.addAll(applicationModel.getEntities().stream().map(EntityModel::getClassName).collect(Collectors.toList()));
        entityNames.addAll(applicationModel.getEntities().stream().map(EntityModel::getEntityName).collect(Collectors.toList()));
        
        application.getEntities().stream().forEach(e -> {
            fireEvent(eventAdminRef, "compiling entity " + e.getName() + " for application " + application.getName());
            compileEntity(application, routerPaths, e);
        });

        repoCompiler = new SkysailRepositoryCompiler(bundle, application);
        repoCompiler.createRepository(entityNames, entityClassNames);

        applicationCompiler = new SkysailApplicationCompiler(bundle, application, routerPaths);
        applicationCompiler.createApplication(applicationModel);
        applicationCompiler.compile(bundle.getBundleContext());

        return applicationCompiler.isCompiledSuccessfully();
    }

    private void compileEntity(Application a, Map<String, String> routerPaths, Entity e) {
        String entityName = getEntityName(a, e);
        //EntityModel entityModel = applicationModel.addEntity(entityName);
        SkysailEntityCompiler entityCompiler = new SkysailEntityCompiler(repo, bundle, a, entityName, e.getName());
        entityCompiler.createEntity(entityNames, entityClassNames);
        entityCompiler.createResources();
        entityCompiler.attachToRouter(router, a.getName(), e, routerPaths);

        handleSubEntities(a, e.getSubEntities(), entityNames, entityClassNames);
    }

    synchronized void setupInMemoryBundle(DbService2 dbService, ComponentContext componentContext) {
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
            setComponentContextMethod.invoke(applicationInstance, new Object[] { componentContext });

            Method activateRepoInstance = dbRepoInstance.getClass().getMethod("activate", new Class[] {});
            activateRepoInstance.invoke(dbRepoInstance, new Object[] {});

            bundle.getBundleContext().registerService(
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
        return e.getName();// sb.toString();
    }

    private void handleSubEntities(Application a, List<Entity> entities, List<String> entityNames,
            List<String> entityClassNames) {
        entities.forEach(sub -> {
            System.out.println(sub);

            String entityName = sub.getName();
            //EntityModel entityModel = applicationModel.addEntity(entityName);
            SkysailSubEntityCompiler entityCompiler = new SkysailSubEntityCompiler(repo, bundle, a, entityName,
                    entityName);
            entityCompiler.setParent("Campaign");
            entityCompiler.createEntity(entityNames, entityClassNames);
            entityCompiler.createResources();
            handleSubEntities(a, sub.getSubEntities(), entityNames, entityClassNames);
        });
    }

    private void fireEvent(AtomicReference<EventAdmin> eventAdminRef, String msg) {
        if (eventAdminRef == null || eventAdminRef.get() == null) {
            return;
        }
        new EventHelper(eventAdminRef.get())//
                .channel(EventHelper.GUI_MSG)//
                .info(msg)//
                .fire();
    }

    private void add(Map<String, String> result, Bundle bundle2, String string) {
        result.put("code/Entity.codegen", BundleUtils.readResource(bundle2, "code/Entity.codegen"));
    }

}
