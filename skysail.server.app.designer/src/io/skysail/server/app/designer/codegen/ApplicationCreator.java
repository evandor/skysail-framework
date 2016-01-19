package io.skysail.server.app.designer.codegen;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.stream.Collectors;

import org.osgi.framework.Bundle;
import org.stringtemplate.v4.ST;

import io.skysail.server.app.designer.EntitiesCreator;
import io.skysail.server.app.designer.RepositoryCreator;
import io.skysail.server.app.designer.STGroupBundleDir;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.codegen.writer.ProjectFileWriter;
import io.skysail.server.app.designer.model.DesignerApplicationModel;
import io.skysail.server.app.designer.model.RouteModel;
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

        List<CompiledCode> repositoriesCode = new RepositoryCreator(applicationModel, javaCompiler, bundle).create(stGroup);
        repositoriesCode.stream().forEach(code -> 
            ProjectFileWriter.save(applicationModel, BUNLDE_DIR_NAME, classNameToPath(code.getClassName()), code.getByteCode()));
        
        createPackageInfoFile(repositoriesCode);
        
        ProjectFileWriter.save(applicationModel, BUNLDE_DIR_NAME, "translations/messages.properties", "".getBytes());
        
        skysailApplicationCompiler = new SkysailApplicationCompiler(applicationModel, stGroup, bundle, javaCompiler);
        compiledApplicationCode = skysailApplicationCompiler.createApplication(routeModels);

        return skysailApplicationCompiler.compile(bundle.getBundleContext());
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

    private void saveClassFiles() {
        compiledApplicationCode.stream().filter(c -> c != null).forEach(code -> 
            ProjectFileWriter.save(applicationModel, BUNLDE_DIR_NAME, classNameToPath(code.getClassName()), code.getByteCode()));
        entitiesCreator.getCode().values().stream().forEach(code -> ProjectFileWriter.save(applicationModel, BUNLDE_DIR_NAME,
                classNameToPath(code.getClassName()), code.getByteCode()));
    }
    
    private String classNameToPath(String className) {
        return Arrays.stream(className.split("\\.")).collect(Collectors.joining("/")).concat(".class");
    }

    private void createBundle() throws FileNotFoundException, IOException {
        Manifest manifest = new Manifest();
        manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
        //manifest.getEntries().put("Bnd-LastModified", new Date().getTime());
        
        Attributes global = manifest.getMainAttributes();
        global.put(new Attributes.Name("Bnd-LastModified"), Long.toString(new Date().getTime()));
        global.put(new Attributes.Name("Bundle-Description"), "skysail application bundle created by the designer");
        global.put(new Attributes.Name("Bundle-License"), "http://www.opensource.org/licenses/apache2.0.php;description=\"Apache 2.0 Licensed\";link=LICENSE");
        global.put(new Attributes.Name("Bundle-ManifestVersion"), "2");
        global.put(new Attributes.Name("Bundle-Name"), "skysail.server.app.wiki");
        global.put(new Attributes.Name("Bundle-SymbolicName"), "skysail.server.app.wiki");
        global.put(new Attributes.Name("Bundle-Version"), "0.1.0");
        
        global.put(new Attributes.Name("Import-Package"), 
                  "de.twenty11.skysail.server.app;version=\"[19.0,20)\","
                + "de.twenty11.skysail.server.core.restlet;version=\"[22.0,23)\","
                + "io.skysail.api.links;version=\"[4.0,5)\","
                + "io.skysail.api.responses;version=\"[18.0,19)\","
                + "io.skysail.domain;version=\"[0.1,1)\","
                + "io.skysail.domain.core;version=\"[1.0,2)\","
                + "io.skysail.domain.core.repos;version=\"[1.0,2)\","
                + "io.skysail.domain.html;version=\"[0.1,1)\","
                + "io.skysail.server.app;version=\"[7.0,8)\","
                + "io.skysail.server.db;version=\"[8.0,9)\","
                + "io.skysail.server.forms;version=\"[5.1,6)\","
                + "io.skysail.server.menus;version=\"[0.1,0.2)\","
                + "io.skysail.server.queryfilter;version=\"[0.5,1)\","
                + "io.skysail.server.restlet.resources;version=\"[7.0,8)\","
                + "javax.persistence;version=\"[2.1,3)\","
                + "org.apache.shiro;version=\"[1.2,2)\","
                + "org.apache.shiro.subject;version=\"[1.2,2)\","
                + "org.osgi.service.event;version=\"[1.3,2)\","
                + "org.restlet,org.restlet.resource,org.osgi.framework;version=\"[1.8,2)\","
                + "javassist.util.proxy");
                
        global.put(new Attributes.Name("Include-Resource"), "templates=src;recursive:=true;filter:=*.st|*.stg");
               
        global.put(new Attributes.Name("Private-Package"), "io.skysail.server.app.wiki;version=\"0.1.0\","
                + "templates.io.skysail.server.app.wiki.pages.resources,"
                + "templates.io.skysail.server.app.wiki.spaces.resources,"
                + "templates.io.skysail.server.app.wiki.versions,translations");
               
        global.put(new Attributes.Name("Provide-Capability"), 
                  "osgi.service;objectClass:List<String>=\"de.twenty11.skysail.server.app.ApplicationProvider,"
                + "io.skysail.server.menus.MenuItemProvider\","
                + "osgi.service;objectClass:List<String>=\"io.skysail.domain.core.repos.DbRepository\"");

        global.put(new Attributes.Name("Require-Capability"), 
                  "osgi.service;filter:=\"(objectClass=io.skysail.domain.core.Repositories)\";effective:=active,"
                + "osgi.service;filter:=\"(objectClass=io.skysail.server.db.DbService)\";effective:=active,"
                + "osgi.ee;filter:=\"(&(osgi.ee=JavaSE)(version=1.8))\"");
        
        global.put(new Attributes.Name("Service-Component"), 
                  "OSGI-INF/io.skysail.server.app.wiki.SpaceRepository.xml,"
                + "OSGI-INF/io.skysail.server.app.wiki.WikiApplication.xml");
        
        global.put(new Attributes.Name("Tool"), "skysail");
        
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
