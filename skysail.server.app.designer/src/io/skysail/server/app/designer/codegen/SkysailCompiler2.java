package io.skysail.server.app.designer.codegen;

import io.skysail.server.app.designer.STGroupBundleDir;
import io.skysail.server.app.designer.model.ApplicationModel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.osgi.framework.BundleContext;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupDir;

@Slf4j
@Getter
public class SkysailCompiler2 {

    @Getter
    protected boolean compiledSuccessfully = true;
    protected ApplicationModel applicationModel;
    protected STGroupDir stGroupDir;

    public SkysailCompiler2(ApplicationModel applicationModel, STGroupBundleDir stGroup) {
        this.applicationModel = applicationModel;
        this.stGroupDir = stGroup;
    }

    public void compile(BundleContext bundleContext) {
        try {
            InMemoryJavaCompiler.compile(bundleContext);
            compiledSuccessfully = InMemoryJavaCompiler.isCompiledSuccessfully();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            compiledSuccessfully = false;
        }
    }

    protected String substitute(String template, Map<String, String> substitutionMap) {
        for (String key : substitutionMap.keySet()) {
            if (substitutionMap.get(key) != null) {
                template = template.replace(key, substitutionMap.get(key));
            }
        }
        return template;
    }

    protected void collect(String className, String entityCode) {
        try {
            InMemoryJavaCompiler.collect(className, entityCode);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        try {
            createProjectIfNeeded();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        String filename = applicationModel.getPath() + "/" + applicationModel.getProjectName() + "/src/"
                + classNameToPath(className);
        filename = filename.replace("//", "/");
        try {
            Path path = Paths.get(filename);
            new File(path.getParent().toString()).mkdirs();
            Files.write(path, entityCode.getBytes());
        } catch (IOException e) {
            log.debug("could not write source code for compilation unit '{}' to '{}'", className, filename);
        }
    }

    private void createProjectIfNeeded() throws IOException {
//        String path = applicationModel.getPath() + "/" + applicationModel.getProjectName();
//        path = path.replace("//", "/");
//        new File(Paths.get(path).toString()).mkdirs();
//        // if (new File(Paths.get(path).toString()).mkdirs()) {
//        String project = BundleUtils.readResource(bundle, "code/project.codegen");
//        project = project.replace("$projectname$", applicationModel.getProjectName() != null ? applicationModel.getProjectName() : "unknown");
//        Files.write(Paths.get(path + "/.project"), project.getBytes());
//
//        String classpath = BundleUtils.readResource(bundle, "code/classpath.codegen");
//        // project.replace("$projectname$", application.getProjectName());
//        Files.write(Paths.get(path + "/.classpath"), classpath.getBytes());
//
//        String bnd = BundleUtils.readResource(bundle, "code/bnd.codegen");
//        bnd = bnd.replace("$packagename$", applicationModel().getPackageName()  != null ? applicationModel.getPackageName() : "unknown");
//        Files.write(Paths.get(path + "/bnd.bnd"), bnd.getBytes());
//
//        String bndrun = BundleUtils.readResource(bundle, "code/bndrun.codegen");
//        bndrun = bndrun.replace("$projectname$", applicationModel.getProjectName() != null ? applicationModel.getProjectName() : "unknown");
//        Files.write(Paths.get(path + "/local.bndrun"), bndrun.getBytes());
//
//        new File(Paths.get(path + "/test").toString()).mkdir();
//        new File(Paths.get(path + "/resources").toString()).mkdir();
//        new File(Paths.get(path + "/config/local").toString()).mkdirs();
//
//        copy(Paths.get(path), "io.skysail.server.db.DbConfigurations-skysailgraph.cfg");
//        copy(Paths.get(path), "logback.xml");
//
//        // Files.walkFileTree(configPath, new CopyDirVisitor(Paths.get("."),
//        // Paths.get(path + "/config/local")));

        // }

    }

   
    public void reset() {
        InMemoryJavaCompiler.reset();
    }

    protected String classNameToPath(String className) {
        return Arrays.stream(className.split("\\.")).collect(Collectors.joining("/")).concat(".java");
    }

    public Class<?> getClass(String className) {
        try {
            return InMemoryJavaCompiler.getClass(className);
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
    
    protected ST getStringTemplateIndex(String root) {
        ST javafile = stGroupDir.getInstanceOf(root);
        javafile.add("application", applicationModel);
        return javafile;
    }
    
 


}
