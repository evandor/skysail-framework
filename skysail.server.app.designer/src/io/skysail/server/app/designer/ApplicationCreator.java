package io.skysail.server.app.designer;

import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.designer.application.Application;
import io.skysail.server.app.designer.codegen.InMemoryJavaCompiler;
import io.skysail.server.app.designer.codegen.SkysailApplicationCompiler2;
import io.skysail.server.app.designer.model.ApplicationModel;
import io.skysail.server.app.designer.model.EntityModel;
import io.skysail.server.app.designer.model.RouteModel;
import io.skysail.server.app.designer.repo.DesignerRepository;
import io.skysail.server.db.DbRepository;
import io.skysail.server.db.DbService2;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import lombok.Getter;

import org.osgi.framework.Bundle;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.event.EventAdmin;

import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.osgi.EventHelper;
import de.twenty11.skysail.server.core.restlet.SkysailRouter;
import de.twenty11.skysail.server.services.MenuItemProvider;

public class ApplicationCreator {

    private Bundle bundle;
    //private SkysailRepositoryCompiler repoCompiler;
    private List<String> entityNames = new ArrayList<>();
    private List<String> entityClassNames = new ArrayList<>();
    private STGroupBundleDir stGroup;
    private SkysailApplicationCompiler2 skysailApplicationCompiler;

    @Getter
    private ApplicationModel applicationModel;
    private Class<?> repositoryClass;
    private String repositoryClassName;

    public ApplicationCreator(Application application, SkysailRouter router, DesignerRepository repo, Bundle bundle) {
        this.bundle = bundle;
        this.applicationModel = new ApplicationModel(application, repo);
        stGroup = new STGroupBundleDir(bundle, "/code2");
    }

    public boolean create(AtomicReference<EventAdmin> eventAdminRef) {

        InMemoryJavaCompiler.reset();

        List<RouteModel> routeModels = new EntityCreator(applicationModel).create(stGroup);
        
        entityClassNames.addAll(applicationModel.getEntities().stream().map(EntityModel::getClassName).collect(Collectors.toList()));
        entityNames.addAll(applicationModel.getEntities().stream().map(EntityModel::getEntityName).collect(Collectors.toList()));
        
        repositoryClassName = new RepositoryCreator(applicationModel).create(stGroup);
        
        skysailApplicationCompiler = new SkysailApplicationCompiler2(applicationModel, stGroup);
        skysailApplicationCompiler.createApplication(routeModels);
        skysailApplicationCompiler.compile(bundle.getBundleContext());

        return skysailApplicationCompiler.isCompiledSuccessfully();
    }

    synchronized void setupInMemoryBundle(DbService2 dbService, ComponentContext componentContext) {
        Class<?> applicationClass = skysailApplicationCompiler.getApplicationClass();
        Class<?> repositoryClass = skysailApplicationCompiler.getClass(repositoryClassName);

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

    private void fireEvent(AtomicReference<EventAdmin> eventAdminRef, String msg) {
        if (eventAdminRef == null || eventAdminRef.get() == null) {
            return;
        }
        new EventHelper(eventAdminRef.get())//
                .channel(EventHelper.GUI_MSG)//
                .info(msg)//
                .fire();
    }

}
