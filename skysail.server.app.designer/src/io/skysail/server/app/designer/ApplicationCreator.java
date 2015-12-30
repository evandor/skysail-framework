package io.skysail.server.app.designer;

import java.io.*;
import java.lang.reflect.*;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

import org.osgi.framework.*;
import org.osgi.service.component.ComponentContext;
import org.stringtemplate.v4.ST;

import de.twenty11.skysail.server.app.ApplicationProvider;
import io.skysail.domain.core.Repositories;
import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.codegen.*;
import io.skysail.server.app.designer.model.*;
import io.skysail.server.app.designer.repo.DesignerRepository;
import io.skysail.server.db.DbService;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.utils.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApplicationCreator {

    private final Bundle bundle;
    private final STGroupBundleDir stGroup;
    private final Repositories repos;

    private SkysailApplicationCompiler skysailApplicationCompiler;
    private List<String> repositoryClassNames;

    @Getter
    private final CodegenApplicationModel applicationModel;

    @Setter
    private BundleResourceReader bundleResourceReader = new DefaultBundleResourceReader();
    
    @Setter
    private JavaCompiler javaCompiler = new DefaultJavaCompiler();

    public ApplicationCreator(DbApplication application, DesignerRepository designerRepository, Repositories repos, Bundle bundle) {
        this.repos = repos;
        this.bundle = bundle;
        this.applicationModel = new CodegenApplicationModel(application);
        stGroup = new STGroupBundleDir(bundle, "/code");
    }

    public boolean createApplication(DbService dbService, ComponentContext componentContext) {
        try {
            createProjectIfNeeded();
            if (createCode()) {
                setupInMemoryBundle(dbService, componentContext);
            }
        } catch (IOException e1) {
            log.error(e1.getMessage(), e1);
        }
        return true;
    }

    private boolean createCode() {
        javaCompiler.reset();

        List<RouteModel> routeModels = new EntityCreator(applicationModel, javaCompiler).create(stGroup);

        repositoryClassNames = new RepositoryCreator(applicationModel, javaCompiler).create(stGroup);

        skysailApplicationCompiler = new SkysailApplicationCompiler(applicationModel, stGroup, javaCompiler);
        skysailApplicationCompiler.createApplication(routeModels);
        skysailApplicationCompiler.compile(bundle.getBundleContext());

        return skysailApplicationCompiler.isCompiledSuccessfully();
    }

    private synchronized void setupInMemoryBundle(DbService dbService, ComponentContext componentContext) {
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
                } catch (Exception e ) {
                   throw new RuntimeException(e.getMessage(), e);
                }
            });

            Method setComponentContextMethod = applicationInstance.getClass().getMethod("setComponentContext",
                    new Class[] { ComponentContext.class });
            setComponentContextMethod.invoke(applicationInstance, new Object[] { componentContext });

            ServiceRegistration<?> registeredService = bundle.getBundleContext().registerService(
                    new String[] { ApplicationProvider.class.getName(), MenuItemProvider.class.getName() },
                    applicationInstance, null);
            
            log.info("new service {} was registered.",registeredService.getReference().toString());

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
        Paths.get(path).toFile().mkdirs();

        ST project = getStringTemplateIndex("project");
        project.add("projectname", applicationModel.getProjectName());
        Path projectFilePath = Paths.get(path + "/.project");
        log.info("creating file '{}'", projectFilePath.toAbsolutePath());
        Files.write(projectFilePath, project.render().getBytes());

        ST classpath = getStringTemplateIndex("classpath");
        Files.write(Paths.get(path + "/.classpath"), classpath.render().getBytes());

        ST bnd = getStringTemplateIndex("bnd");
        bnd.add("packagename", applicationModel.getPackageName());
        Files.write(Paths.get(path + "/bnd.bnd"), bnd.render().getBytes());

        ST bndrun = getStringTemplateIndex("bndrun");
        bndrun.add("projectname", applicationModel.getProjectName());
        Files.write(Paths.get(path + "/test.bndrun"), bndrun.render().getBytes());

        //ST gradle = getStringTemplateIndex("gradle");
        //Files.write(Paths.get(path + "/build.gradle"), gradle.render().getBytes());

        new File(Paths.get(path + "/test").toString()).mkdir();
        new File(Paths.get(path + "/src-gen").toString()).mkdir();
        new File(Paths.get(path + "/resources").toString()).mkdir();
        new File(Paths.get(path + "/config/local").toString()).mkdirs();

        Files.write(Paths.get(path + "/resources/.gitignore"), "".getBytes());

        copy(Paths.get(path), "io.skysail.server.db.DbConfigurations-skysailgraph.cfg");
        copy(Paths.get(path), "logback.xml");
    }

    private void copy(Path path, String filename) {
        String cfgFile = bundleResourceReader.readResource(bundle, "config/" + filename);
        try {
            Files.write(Paths.get(path + "/config/local/" + filename), cfgFile.getBytes());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private ST getStringTemplateIndex(String root) {
        return stGroup.getInstanceOf(root);
    }

}
