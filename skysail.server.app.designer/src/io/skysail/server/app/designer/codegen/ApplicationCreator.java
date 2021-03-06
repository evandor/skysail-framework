package io.skysail.server.app.designer.codegen;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.jar.JarOutputStream;
import java.util.stream.Collectors;

import org.osgi.framework.Bundle;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.stringtemplate.v4.ST;

import io.skysail.server.app.designer.EntitiesCreator;
import io.skysail.server.app.designer.RepositoryCreator;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.codegen.templates.TemplateProvider;
import io.skysail.server.app.designer.codegen.writer.JarWriter;
import io.skysail.server.app.designer.codegen.writer.ProjectFileWriter;
import io.skysail.server.app.designer.model.DesignerApplicationModel;
import io.skysail.server.app.designer.model.RouteModel;
import io.skysail.server.utils.BundleResourceReader;
import io.skysail.server.utils.DefaultBundleResourceReader;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Creates a new skysail application project (and OSGi bundle) from an application model 
 * defined by an DbApplication entity.
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
@Component(immediate = true, service = ApplicationCreator.class)
@NoArgsConstructor
public class ApplicationCreator {

    private static final String BUNLDE_DIR_NAME = "bundle";
    
    @Getter
    private DesignerApplicationModel applicationModel;
    
    @Setter
    private BundleResourceReader bundleResourceReader = new DefaultBundleResourceReader();
    
    @Setter
    private JavaCompiler javaCompiler = new DefaultJavaCompiler();
    
    private Bundle bundle;
    private SkysailApplicationCompiler skysailApplicationCompiler;
    private EntitiesCreator entitiesCreator;
    private List<CompiledCode> compiledApplicationCode;
    private List<CompiledCode> repositoriesCode;
    private List<CompiledCode> entitiesCode;
    
    @Reference
    private TemplateProvider templateProvider;


    @Activate
    public void activate(ComponentContext componentContext) {
        bundle = componentContext.getBundleContext().getBundle();
    }
    
    @Deactivate
    public void deactivate() {
        templateProvider = null;
        bundle = null;
    }

    /**
     * creating a skysail application is done in a couple of steps: a project
     * structure is set up on disk, the java code is created in memory (and
     * stored to the project structure as java source files), and, if this was
     * successful, a new bundle jar is created from the class (and additional)
     * files.
     */
    public boolean createApplication(DbApplication application) {
        this.applicationModel = new DesignerApplicationModel(application);
        templateProvider.add("application", applicationModel);
        try {
            createProjectStructure();
            if (!createCode()) {
                return false;
            }
            if (doNotCreateBundle()) {
                return true;
            }
            saveClassFiles();
            createBundle();
        } catch (IOException e1) {
            log.error(e1.getMessage(), e1);
        }
        return true;
    }

    private boolean createCode() {
        javaCompiler.reset();

        entitiesCreator = new EntitiesCreator(applicationModel, javaCompiler, templateProvider);
        List<RouteModel> routeModels = entitiesCreator.create(null);
        entitiesCode = entitiesCreator.getCode();//.stream().collect(Collectors.toMap(CompiledCode::getName, Function.identity()));

        repositoriesCode = new RepositoryCreator(applicationModel, javaCompiler, bundle, templateProvider).create(null);
        
        createPackageInfoFile(repositoriesCode);
        
        ProjectFileWriter.save(applicationModel, BUNLDE_DIR_NAME, "translations/messages.properties", "\n".getBytes());
        ProjectFileWriter.save(applicationModel, BUNLDE_DIR_NAME, "templates/.gitignore", "\n".getBytes());
        
        skysailApplicationCompiler = new SkysailApplicationCompiler(applicationModel, null, bundle, javaCompiler, templateProvider);
        compiledApplicationCode = skysailApplicationCompiler.createApplication(routeModels);
        
        //SkysailTestCompiler skysailTestCompiler = new SkysailTestCompiler(applicationModel, stGroup, bundle, javaCompiler);
        //List<CompiledCode> compiledTestCode = skysailTestCompiler.createTests(routeModels);

        return skysailApplicationCompiler.compile(bundle.getBundleContext());
    }
        
    private boolean doNotCreateBundle() {
        Optional<Bundle> existingBundle = Arrays.stream(bundle.getBundleContext().getBundles()).filter(b -> b.getSymbolicName().equals(applicationModel.getProjectName())).findFirst();
        if (existingBundle.isPresent()) {
            String tool = existingBundle.get().getHeaders().get("Tool");
            if (tool.contains("skysail")) {
                return false;
            }
            return true;
        }
        return false;
    }
    
    private void saveClassFiles() {
        compiledApplicationCode.stream().filter(c -> c != null).forEach(code -> 
            ProjectFileWriter.save(applicationModel, BUNLDE_DIR_NAME, classNameToPath(code.getClassName()), code.getByteCode()));
//        entitiesCreator.getCode().values().stream().forEach(code -> 
//            ProjectFileWriter.save(applicationModel, BUNLDE_DIR_NAME, classNameToPath(code.getClassName()), code.getByteCode()));
        entitiesCode.stream().forEach(code ->
            ProjectFileWriter.save(applicationModel, BUNLDE_DIR_NAME, classNameToPath(code.getClassName()), code.getByteCode()));
        repositoriesCode.stream().forEach(code -> 
            ProjectFileWriter.save(applicationModel, BUNLDE_DIR_NAME, classNameToPath(code.getClassName()), code.getByteCode()));
    }

    private void createPackageInfoFile(List<CompiledCode> repositoriesCode) {
        if (repositoriesCode.isEmpty()) {
            return;
        }
        String firstClassName = repositoriesCode.get(0).getClassName();
        String firstClassNamePath = Arrays.stream(firstClassName.split("\\.")).collect(Collectors.joining("/"));
        String thePath = firstClassNamePath.substring(0,firstClassNamePath.lastIndexOf("/"));
        ProjectFileWriter.save(applicationModel, BUNLDE_DIR_NAME, thePath + "/packageinfo", "version 0.1.0".getBytes());
    }

    private String classNameToPath(String className) {
        return Arrays.stream(className.split("\\.")).collect(Collectors.joining("/")).concat(".class");
    }

    private void createBundle() throws IOException {
        JarOutputStream bundleJar = JarWriter.startBundleJar(applicationModel);
        String projectPath = ProjectFileWriter.getProjectPath(applicationModel).replace("//", "/") + "/" + BUNLDE_DIR_NAME;
        JarWriter.add(bundleJar, projectPath.replace("\\\\", "/"));
        bundleJar.close();
    }

    private void createProjectStructure() throws IOException {
        String root = applicationModel.getPath() + "/" + applicationModel.getProjectName();
        
        ProjectFileWriter.mkdirs(root);

        createEclipseArtifacts(root);
        createBndFileIfNotExisting(root);
        createBndrunFile(root);

        ProjectFileWriter.deleteDir(root + "/src-gen");
        ProjectFileWriter.deleteDir(root + "/test-gen");
        ProjectFileWriter.deleteDir(root + "/" + BUNLDE_DIR_NAME);
        
        ProjectFileWriter.mkdirs(root + "/test");
        ProjectFileWriter.mkdirs(root + "/src-gen");
        ProjectFileWriter.mkdirs(root + "/test-gen");
        ProjectFileWriter.mkdirs(root + "/" + BUNLDE_DIR_NAME);
        ProjectFileWriter.mkdirs(root + "/resources");
        ProjectFileWriter.mkdirs(root + "/config/local");

        createGitIgnoreFile(root);

        copy(Paths.get(root), "db-default.cfg");
        copy(Paths.get(root), "logback.xml");
    }

    private void createEclipseArtifacts(String root) throws IOException {
        ST project = templateProvider.templateFor("project");
        project.add("projectname", applicationModel.getProjectName());
        Path dotProjectFilePath = Paths.get(root + "/.project");
        log.info("creating file '{}'", dotProjectFilePath.toAbsolutePath());
        Files.write(dotProjectFilePath, project.render().getBytes());

        ST classpath = templateProvider.templateFor("classpath");
        Files.write(Paths.get(root + "/.classpath"), classpath.render().getBytes());
    }
    
    private void createBndFileIfNotExisting(String root) throws IOException {
        Path bndPath = Paths.get(root + "/bnd.bnd");
        if (bndPath.toFile().exists()) {
            log.debug("did not create file '{}' as it already exists", bndPath.toString());
            return;
        }
        ST bnd = templateProvider.templateFor("bnd");
        bnd.add("packagename", applicationModel.getPackageName());
        Files.write(bndPath, bnd.render().getBytes());
    }

    private void createBndrunFile(String root) throws IOException {
        ST bndrun = templateProvider.templateFor("bndrun");
        bndrun.add("name", applicationModel.getName());
        bndrun.add("projectName", applicationModel.getProjectName());
        Files.write(Paths.get(root + "/test.bndrun"), bndrun.render().getBytes());
    }
    
    private static void createGitIgnoreFile(String root) throws IOException {
        Files.write(Paths.get(root + "/resources/.gitignore"),
                "".getBytes());
        Files.write(Paths.get(root + "/.gitignore"),
                "/bin/\n/bin_test/\n/generated/\n/bundle/".getBytes());
    }

    private void copy(Path path, String filename) {
        String cfgFile = bundleResourceReader.readResource(bundle, "config/" + filename);
        try {
            Files.write(Paths.get(path + "/config/local/" + filename), cfgFile.getBytes());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
