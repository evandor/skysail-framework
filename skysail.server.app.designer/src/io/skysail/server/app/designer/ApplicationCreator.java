package io.skysail.server.app.designer;

import io.skysail.api.repos.DbRepository;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.designer.application.Application;
import io.skysail.server.app.designer.codegen.*;
import io.skysail.server.app.designer.model.*;
import io.skysail.server.app.designer.repo.DesignerRepository;
import io.skysail.server.db.DbService;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.utils.BundleUtils;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import lombok.Getter;

import org.osgi.framework.Bundle;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.event.EventAdmin;
import org.stringtemplate.v4.ST;

import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.osgi.EventHelper;
import de.twenty11.skysail.server.core.restlet.SkysailRouter;

public class ApplicationCreator {

    private Bundle bundle;
    private List<String> entityNames = new ArrayList<>();
    private List<String> entityClassNames = new ArrayList<>();
    private STGroupBundleDir stGroup;
    private SkysailApplicationCompiler2 skysailApplicationCompiler;
    private String repositoryClassName;

    @Getter
    private ApplicationModel applicationModel;

    public ApplicationCreator(Application application, SkysailRouter router, DesignerRepository repo, Bundle bundle) {
        this.bundle = bundle;
        this.applicationModel = new ApplicationModel(application, repo);
        stGroup = new STGroupBundleDir(bundle, "/code2");
    }

    public boolean create(AtomicReference<EventAdmin> eventAdminRef) {

        try {
            createProjectIfNeeded();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        InMemoryJavaCompiler.reset();

        List<RouteModel> routeModels = new EntityCreator(applicationModel).create(stGroup);

        entityClassNames.addAll(applicationModel.getEntityModels().stream().map(EntityModel::getClassName).collect(Collectors.toList()));
        entityNames.addAll(applicationModel.getEntityModels().stream().map(EntityModel::getEntityName).collect(Collectors.toList()));

        repositoryClassName = new RepositoryCreator(applicationModel).create(stGroup);

        skysailApplicationCompiler = new SkysailApplicationCompiler2(applicationModel, stGroup);
        skysailApplicationCompiler.createApplication(routeModels);
        skysailApplicationCompiler.compile(bundle.getBundleContext());

        return skysailApplicationCompiler.isCompiledSuccessfully();
    }

    synchronized void setupInMemoryBundle(DbService dbService, ComponentContext componentContext) {
        Class<?> applicationClass = skysailApplicationCompiler.getApplicationClass();
        Class<?> repositoryClass = skysailApplicationCompiler.getClass(repositoryClassName);

        try {
            SkysailApplication applicationInstance = (SkysailApplication) applicationClass.newInstance();
            DbRepository dbRepoInstance = (DbRepository) repositoryClass.newInstance();
            //System.out.println(applicationInstance);

            Method setDbServiceMethod = dbRepoInstance.getClass().getMethod("setDbService",
                    new Class[] { DbService.class });
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

    private void createProjectIfNeeded() throws IOException {
        String path = applicationModel.getPath() + "/" + applicationModel.getProjectName();
        path = path.replace("//", "/");
        new File(Paths.get(path).toString()).mkdirs();

        ST project = getStringTemplateIndex("project");
        project.add("projectname", applicationModel.getProjectName());
        Files.write(Paths.get(path + "/.project"), project.render().getBytes());

        ST classpath = getStringTemplateIndex("classpath");
        Files.write(Paths.get(path + "/.classpath"), classpath.render().getBytes());

        ST bnd = getStringTemplateIndex("bnd");
        bnd.add("packagename", applicationModel.getPackageName());
        Files.write(Paths.get(path + "/bnd.bnd"), bnd.render().getBytes());

        ST bndrun = getStringTemplateIndex("bndrun");
        bndrun.add("projectname", applicationModel.getProjectName());
        Files.write(Paths.get(path + "/bndrun.bnd"), bndrun.render().getBytes());

        ST gradle = getStringTemplateIndex("gradle");
        //gradle.add("projectname", applicationModel.getProjectName());
        Files.write(Paths.get(path + "/build.gradle"), gradle.render().getBytes());

        new File(Paths.get(path + "/test").toString()).mkdir();
        new File(Paths.get(path + "/src-gen").toString()).mkdir();
        new File(Paths.get(path + "/resources").toString()).mkdir();
        new File(Paths.get(path + "/config/local").toString()).mkdirs();

        Files.write(Paths.get(path + "/resources/.gitignore"), "".getBytes());

        copy(Paths.get(path), "io.skysail.server.db.DbConfigurations-skysailgraph.cfg");
        copy(Paths.get(path), "logback.xml");
    }

    private void copy(Path path, String filename) {
        String cfgFile = BundleUtils.readResource(bundle, "config/" + filename);
        try {
            Files.write(Paths.get(path + "/config/local/" + filename), cfgFile.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ST getStringTemplateIndex(String root) {
        ST javafile = stGroup.getInstanceOf(root);
        //javafile.add("application", applicationModel);
        return javafile;
    }


}
