package io.skysail.server.app.designer;

import java.io.*;
import java.lang.reflect.*;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

import org.osgi.framework.Bundle;
import org.osgi.service.component.ComponentContext;
import org.stringtemplate.v4.ST;

import de.twenty11.skysail.server.app.ApplicationProvider;
import io.skysail.api.repos.DbRepository;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.designer.application.Application;
import io.skysail.server.app.designer.codegen.*;
import io.skysail.server.app.designer.model.*;
import io.skysail.server.app.designer.repo.DesignerRepository;
import io.skysail.server.db.DbService;
import io.skysail.server.domain.core.Repositories;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.utils.BundleUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApplicationCreator {

    private Bundle bundle;
    private STGroupBundleDir stGroup;
    private SkysailApplicationCompiler skysailApplicationCompiler;
    private List<String> repositoryClassNames;

    @Getter
    private CodegenApplicationModel applicationModel;
    private Repositories repos;

    public ApplicationCreator(Application application, DesignerRepository designerRepository, Repositories repos, Bundle bundle) {
        this.repos = repos;
        this.bundle = bundle;
        this.applicationModel = new CodegenApplicationModel(application, designerRepository);
        stGroup = new STGroupBundleDir(bundle, "/code");
    }

    public boolean create() {
        try {
            createProjectIfNeeded();
            return createCode();
        } catch (IOException e1) {
            log.error(e1.getMessage(), e1);
        }
        return false;
    }

    private boolean createCode() {
        InMemoryJavaCompiler.reset();

        List<RouteModel> routeModels = new EntityCreator(applicationModel).create(stGroup);

        repositoryClassNames = new RepositoryCreator(applicationModel).create(stGroup);

        skysailApplicationCompiler = new SkysailApplicationCompiler(applicationModel, stGroup);
        skysailApplicationCompiler.createApplication(routeModels);
        skysailApplicationCompiler.compile(bundle.getBundleContext());

        return skysailApplicationCompiler.isCompiledSuccessfully();
    }

    synchronized void setupInMemoryBundle(DbService dbService, ComponentContext componentContext) {
        Class<?> applicationClass = skysailApplicationCompiler.getApplicationClass();

        List<Class<?>> repositoryClasses = repositoryClassNames.stream()
                .map(repoName -> skysailApplicationCompiler.getClass(repoName)).collect(Collectors.toList());

        try {
            SkysailApplication applicationInstance = (SkysailApplication) applicationClass.newInstance();

            repositoryClasses.stream().forEach(repositoryClass -> {
                try {
                    DbRepository dbRepoInstance = (DbRepository) repositoryClass.newInstance();
                    setDbServiceInRepository(dbService, dbRepoInstance);
                    repos.setRepository(dbRepoInstance);
                    setRepositoriesInApplication(applicationInstance);
                    activateRepository(dbRepoInstance);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            });

            Method setComponentContextMethod = applicationInstance.getClass().getMethod("setComponentContext",
                    new Class[] { ComponentContext.class });
            setComponentContextMethod.invoke(applicationInstance, new Object[] { componentContext });

            bundle.getBundleContext().registerService(
                    new String[] { ApplicationProvider.class.getName(), MenuItemProvider.class.getName() },
                    applicationInstance, null);

        } catch (Exception e1) {
            log.error(e1.getMessage(), e1);
        }
    }

    private void activateRepository(DbRepository dbRepoInstance)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method activateRepoInstance = dbRepoInstance.getClass().getMethod("activate", new Class[] {});
        activateRepoInstance.invoke(dbRepoInstance, new Object[] {});
    }

    private void setRepositoriesInApplication(SkysailApplication applicationInstance)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method setRepositoryMethod = applicationInstance.getClass().getMethod("setRepositories",
                new Class[] { Repositories.class });
        setRepositoryMethod.invoke(applicationInstance, repos);
    }

    private void setDbServiceInRepository(DbService dbService, DbRepository dbRepoInstance)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method setDbServiceMethod = dbRepoInstance.getClass().getMethod("setDbService",
                new Class[] { DbService.class });
        setDbServiceMethod.invoke(dbRepoInstance, dbService);
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
            log.error(e.getMessage(), e);
        }
    }

    private ST getStringTemplateIndex(String root) {
        ST javafile = stGroup.getInstanceOf(root);
        // javafile.add("application", applicationModel);
        return javafile;
    }

}
