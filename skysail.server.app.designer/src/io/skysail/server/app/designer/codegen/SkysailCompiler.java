package io.skysail.server.app.designer.codegen;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

import org.osgi.framework.BundleContext;
import org.stringtemplate.v4.*;

import io.skysail.server.app.designer.STGroupBundleDir;
import io.skysail.server.app.designer.model.DesignerApplicationModel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class SkysailCompiler {

    @Getter
    protected boolean compiledSuccessfully = true;
    protected DesignerApplicationModel applicationModel;
    protected STGroupDir stGroupDir;
    private JavaCompiler compiler;
    
    public SkysailCompiler(DesignerApplicationModel applicationModel, STGroupBundleDir stGroup, JavaCompiler compiler) {
        this.applicationModel = applicationModel;
        this.stGroupDir = stGroup;
        this.compiler = compiler;
    }

    public void compile(BundleContext bundleContext) {
        compiledSuccessfully = compiler.compile(bundleContext);
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
            compiler.collect(className, entityCode);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
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
   
    public void reset() {
        compiler.reset();
    }

    protected String classNameToPath(String className) {
        return Arrays.stream(className.split("\\.")).collect(Collectors.joining("/")).concat(".java");
    }

    public Class<?> getClass(String className) {
        try {
            return compiler.getClass(className);
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
