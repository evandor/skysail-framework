package io.skysail.server.app.designer.codegen;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.stream.Collectors;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;
import org.stringtemplate.v4.ST;

import de.twenty11.skysail.server.app.ApplicationProvider;
import io.skysail.domain.core.Repositories;
import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.designer.EntitiesCreator;
import io.skysail.server.app.designer.RepositoryCreator;
import io.skysail.server.app.designer.STGroupBundleDir;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.codegen.writer.ProjectFileWriter;
import io.skysail.server.app.designer.model.DesignerApplicationModel;
import io.skysail.server.app.designer.model.RouteModel;
import io.skysail.server.db.DbService;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.utils.BundleResourceReader;
import io.skysail.server.utils.DefaultBundleResourceReader;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Creates a new skysail application project from an application model defined by an
 * DbApplication object.
 * 
 * The projects location (as everything else) is defined by the application model; the
 * project itself can be directly imported to an eclipse bndtools workspace.
 *  
 * Furthermore, all runtime artifacts (class files, xml files, manifest etc) are created in 
 * a "bundle" subfolder, which is used to create a bundle jar file which can be used in a
 * skysail server runtime right away.
 *
 */
@Slf4j
public class ApplicationCreator {

    private final Bundle bundle;
    private final STGroupBundleDir stGroup;
    private final Repositories repos;

    private SkysailApplicationCompiler skysailApplicationCompiler;
    private List<String> repositoryClassNames;

    @Getter
    private final DesignerApplicationModel applicationModel;

    @Setter
    private BundleResourceReader bundleResourceReader = new DefaultBundleResourceReader();

    @Setter
    private JavaCompiler javaCompiler = new DefaultJavaCompiler();

    private EntitiesCreator entitiesCreator;
    private CompiledCode compiledApplicationCode;

    public ApplicationCreator(DbApplication application, Repositories repos, Bundle bundle) {
        this.repos = repos;
        this.bundle = bundle;
        this.applicationModel = new DesignerApplicationModel(application);
        stGroup = new STGroupBundleDir(bundle, "/code");
    }

    /**
     * creating a skysail application is done in a couple of steps: a project
     * structure is set up on disk, the java code is created in memory (and
     * stored to the project structure as java source files), and, if this was
     * successful, a new bundle jar is created from the class files (and
     * additional files).
     */
    public boolean createApplication(DbService dbService, ComponentContext componentContext) {
        try {
            createProjectStructure();
            if (createCode()) {
                saveClassFiles();
                createDSFiles();
                createBundle();
                setupInMemoryBundle(dbService, componentContext);
            }
        } catch (IOException e1) {
            log.error(e1.getMessage(), e1);
        }
        return true;
    }

    private boolean createCode() {
        javaCompiler.reset();

        entitiesCreator = new EntitiesCreator(applicationModel, javaCompiler);
        List<RouteModel> routeModels = entitiesCreator.create(stGroup);

        repositoryClassNames = new RepositoryCreator(applicationModel, javaCompiler, bundle).create(stGroup);

        skysailApplicationCompiler = new SkysailApplicationCompiler(applicationModel, stGroup, bundle, javaCompiler);
        compiledApplicationCode = skysailApplicationCompiler.createApplication(routeModels);
        skysailApplicationCompiler.compile(bundle.getBundleContext());

        return skysailApplicationCompiler.isCompiledSuccessfully();
    }

    private void saveClassFiles() {
        ProjectFileWriter.save(applicationModel, "bundle", classNameToPath(compiledApplicationCode.getClassName()), compiledApplicationCode.getByteCode());
        entitiesCreator.getCode().values().stream().forEach(code -> ProjectFileWriter.save(applicationModel, "bundle",
                classNameToPath(code.getClassName()), code.getByteCode()));
    }
    
    private void createDSFiles() {
        
    }

    private String classNameToPath(String className) {
        return Arrays.stream(className.split("\\.")).collect(Collectors.joining("/")).concat(".class");
    }

    private void createBundle() throws FileNotFoundException, IOException {
        Manifest manifest = new Manifest();
        manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
        //manifest.getEntries().put("Bnd-LastModified", new Date().getTime());
        String projectName = applicationModel.getProjectName();
        JarOutputStream bundleJar = new JarOutputStream(new FileOutputStream("designerbundles/" + projectName + ".jar"),
                manifest);
        String projectPath = ProjectFileWriter.getProjectPath(applicationModel).replace("//", "/");
        add(new File(projectPath + "/bundle"), projectPath + "/bundle", ".", bundleJar);
        bundleJar.close();
    }

    private static void add(File source, String rootPath, String jarPath, JarOutputStream target) throws IOException {
        BufferedInputStream in = null;
        try {
            if (source.isDirectory()) {
                String name = source.getPath().replace("\\", "/");
                if (!name.isEmpty()) {
                    if (!name.endsWith("/")) {
                        name += "/";
                    }
                    if (!jarPath.endsWith("/")) {
                        jarPath += "/";
                    }
                    JarEntry entry = new JarEntry(jarPath);
                    entry.setTime(source.lastModified());
                    target.putNextEntry(entry);
                    target.closeEntry();
                }
                for (File nestedFile : source.listFiles()) {
                    jarPath = "." + nestedFile.toString().substring(rootPath.length());
                    add(nestedFile, rootPath, jarPath, target);
                }
                return;
            }

            JarEntry entry = new JarEntry(jarPath);//source.getPath().replace("\\", "/"));
            entry.setTime(source.lastModified());
            target.putNextEntry(entry);
            in = new BufferedInputStream(new FileInputStream(source));

            byte[] buffer = new byte[1024];
            while (true) {
                int count = in.read(buffer);
                if (count == -1)
                    break;
                target.write(buffer, 0, count);
            }
            target.closeEntry();
        } finally {
            if (in != null)
                in.close();
        }
    }

    private synchronized void setupInMemoryBundle(DbService dbService, ComponentContext componentContext) {
        Class<?> applicationClass = skysailApplicationCompiler.getApplicationClass();

        List<Class<?>> repositoryClasses = repositoryClassNames.stream()
                .map(repoName -> skysailApplicationCompiler.getClass(repoName)).collect(Collectors.toList()); // NOSONAR

        try {
            SkysailApplication applicationInstance = (SkysailApplication) applicationClass.newInstance();
            repositoryClasses.stream().forEach(repositoryClass -> {
                try { // NOSONAR
                    DbRepository dbRepoInstance = (DbRepository) repositoryClass.newInstance();
                    setDbServiceInRepository(dbService, dbRepoInstance);
                    repos.setRepository(dbRepoInstance);
                    setRepositoriesInApplication(applicationInstance);
                    activateRepository(dbRepoInstance);
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage(), e); // NOSONAR
                }
            });

            Method setComponentContextMethod = applicationInstance.getClass().getMethod("setComponentContext",
                    new Class[] { ComponentContext.class });
            setComponentContextMethod.invoke(applicationInstance, new Object[] { componentContext });

            Collection<ServiceReference<ApplicationProvider>> appProviders = bundle.getBundleContext()
                    .getServiceReferences(ApplicationProvider.class, null);
            appProviders.stream().forEach(appProvider -> {
                System.out.println(Arrays.stream(appProvider.getPropertyKeys())
                        .map(key -> key + ": " + appProvider.getProperty(key)).collect(Collectors.joining(", ")));
            });
            List<ServiceReference<ApplicationProvider>> virtualProviders = appProviders.stream().filter(appProvider -> {
                return appProvider.getProperty("component.id") == null;
            }).collect(Collectors.toList());

            virtualProviders.stream().forEach(p -> bundle.getBundleContext().ungetService(p));

            ServiceRegistration<?> registeredService = bundle.getBundleContext().registerService(
                    new String[] { ApplicationProvider.class.getName(), MenuItemProvider.class.getName() },
                    applicationInstance, null);

            log.info("new service {} was registered.", registeredService.getReference().toString());

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

    private void createProjectStructure() throws IOException {
        String root = applicationModel.getPath() + "/" + applicationModel.getProjectName();
        
        ProjectFileWriter.mkdirs(root);

        ST project = getStringTemplateIndex("project");
        project.add("projectname", applicationModel.getProjectName());
        Path projectFilePath = Paths.get(root + "/.project");
        log.info("creating file '{}'", projectFilePath.toAbsolutePath());
        Files.write(projectFilePath, project.render().getBytes());

        ST classpath = getStringTemplateIndex("classpath");
        Files.write(Paths.get(root + "/.classpath"), classpath.render().getBytes());

        ST bnd = getStringTemplateIndex("bnd");
        bnd.add("packagename", applicationModel.getPackageName());
        Files.write(Paths.get(root + "/bnd.bnd"), bnd.render().getBytes());

        ST bndrun = getStringTemplateIndex("bndrun");
        bndrun.add("name", applicationModel.getName());
        bndrun.add("projectName", applicationModel.getProjectName());
        Files.write(Paths.get(root + "/test.bndrun"), bndrun.render().getBytes());

        ProjectFileWriter.deleteDir(root + "/src-gen");
        ProjectFileWriter.deleteDir(root + "/bundle");
        
        ProjectFileWriter.mkdirs(root + "/test");
        ProjectFileWriter.mkdirs(root + "/src-gen");
        ProjectFileWriter.mkdirs(root + "/bundle");
        ProjectFileWriter.mkdirs(root + "/resources");
        ProjectFileWriter.mkdirs(root + "/config/local");

        Files.write(Paths.get(root + "/resources/.gitignore"),
                "/bin/\n/bin_test/\n/generated/\n/src-gen/\n/bundle/".getBytes());

        copy(Paths.get(root), "io.skysail.server.db.DbConfigurations-skysailgraph.cfg");
        copy(Paths.get(root), "logback.xml");
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
