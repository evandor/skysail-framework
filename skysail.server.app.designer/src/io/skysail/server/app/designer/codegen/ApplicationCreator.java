package io.skysail.server.app.designer.codegen;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.jar.JarOutputStream;
import java.util.stream.Collectors;

import org.osgi.framework.Bundle;
import org.stringtemplate.v4.ST;

import io.skysail.server.app.designer.*;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.codegen.writer.*;
import io.skysail.server.app.designer.model.*;
import io.skysail.server.utils.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * Creates a new skysail application project (and bundle) from an application model 
 * defined by an DbApplication object.
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

    private static final String BUNLDE_DIR_NAME = "bundle";
    
    @Getter
    private final DesignerApplicationModel applicationModel;
    
    @Setter
    private BundleResourceReader bundleResourceReader = new DefaultBundleResourceReader();
    
    @Setter
    private JavaCompiler javaCompiler = new DefaultJavaCompiler();
    
    private final Bundle bundle;
    private final STGroupBundleDir stGroup;

    private SkysailApplicationCompiler skysailApplicationCompiler;
    private EntitiesCreator entitiesCreator;
    private List<CompiledCode> compiledApplicationCode;
    private List<CompiledCode> repositoriesCode;

    public ApplicationCreator(DbApplication application, Bundle bundle) {
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
    public boolean createApplication() {
        try {
            createProjectStructure();
            if (createCode()) {
                if (doNotCreateBundle()) {
                    return true;
                }
                saveClassFiles();
                createBundle();
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

        repositoriesCode = new RepositoryCreator(applicationModel, javaCompiler, bundle).create(stGroup);
        
        createPackageInfoFile(repositoriesCode);
        
        ProjectFileWriter.save(applicationModel, BUNLDE_DIR_NAME, "translations/messages.properties", "\n".getBytes());
        ProjectFileWriter.save(applicationModel, BUNLDE_DIR_NAME, "templates/.gitignore", "\n".getBytes());
        
        skysailApplicationCompiler = new SkysailApplicationCompiler(applicationModel, stGroup, bundle, javaCompiler);
        compiledApplicationCode = skysailApplicationCompiler.createApplication(routeModels);

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
        entitiesCreator.getCode().values().stream().forEach(code -> 
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

    private void createBundle() throws FileNotFoundException, IOException {
        JarOutputStream bundleJar = JarWriter.startBundleJar(applicationModel);
        JarWriter.add(bundleJar, ProjectFileWriter.getProjectPath(applicationModel).replace("//", "/") + "/" + BUNLDE_DIR_NAME);
        bundleJar.close();
    }

    private void createProjectStructure() throws IOException {
        String root = applicationModel.getPath() + "/" + applicationModel.getProjectName();
        
        ProjectFileWriter.mkdirs(root);

        createEclipseArtifacts(root);
        createBndFile(root);
        createBndrunFile(root);

        ProjectFileWriter.deleteDir(root + "/src-gen");
        ProjectFileWriter.deleteDir(root + "/" + BUNLDE_DIR_NAME);
        
        ProjectFileWriter.mkdirs(root + "/test");
        ProjectFileWriter.mkdirs(root + "/src-gen");
        ProjectFileWriter.mkdirs(root + "/" + BUNLDE_DIR_NAME);
        ProjectFileWriter.mkdirs(root + "/resources");
        ProjectFileWriter.mkdirs(root + "/config/local");

        createGitIgnoreFile(root);

        copy(Paths.get(root), "io.skysail.server.db.DbConfigurations-skysailgraph.cfg");
        copy(Paths.get(root), "logback.xml");
    }

    private void createEclipseArtifacts(String root) throws IOException {
        ST project = getStringTemplateIndex("project");
        project.add("projectname", applicationModel.getProjectName());
        Path dotProjectFilePath = Paths.get(root + "/.project");
        log.info("creating file '{}'", dotProjectFilePath.toAbsolutePath());
        Files.write(dotProjectFilePath, project.render().getBytes());

        ST classpath = getStringTemplateIndex("classpath");
        Files.write(Paths.get(root + "/.classpath"), classpath.render().getBytes());
    }
    
    private void createBndFile(String root) throws IOException {
        ST bnd = getStringTemplateIndex("bnd");
        bnd.add("packagename", applicationModel.getPackageName());
        Files.write(Paths.get(root + "/bnd.bnd"), bnd.render().getBytes());
    }

    private void createBndrunFile(String root) throws IOException {
        ST bndrun = getStringTemplateIndex("bndrun");
        bndrun.add("name", applicationModel.getName());
        bndrun.add("projectName", applicationModel.getProjectName());
        Files.write(Paths.get(root + "/test.bndrun"), bndrun.render().getBytes());
    }
    
    private void createGitIgnoreFile(String root) throws IOException {
        Files.write(Paths.get(root + "/resources/.gitignore"),
                "/bin/\n/bin_test/\n/generated/\n/src-gen/\n/bundle/".getBytes());
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
