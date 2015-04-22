package io.skysail.server.app.designer.codegen;

import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.PostDynamicEntityServerResource;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.server.utils.CompositeClassLoader;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.tools.DiagnosticCollector;
import javax.tools.DiagnosticListener;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import javax.validation.ConstraintViolation;

import lombok.extern.slf4j.Slf4j;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.restlet.resource.ServerResource;

// credit goes to https://github.com/trung/InMemoryJavaCompiler/blob/master/src/main/java/org/mdkt/compiler/InMemoryJavaCompiler.java

@Slf4j
public class InMemoryJavaCompiler {

    static JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
    
    static DynamicClassLoader dcl;

    private static List<SourceCode> sourceCodes = new ArrayList<>();

    static {
        CompositeClassLoader customCL = new CompositeClassLoader();
        customCL.addClassLoader(Thread.currentThread().getContextClassLoader());
        customCL.addClassLoader(ListServerResource.class.getClassLoader());
        customCL.addClassLoader(DesignerApplication.class.getClassLoader());
        customCL.addClassLoader(PostDynamicEntityServerResource.class.getClassLoader());
        //customCL.addClassLoader(javax.validation.ConstraintViolation.class.getClassLoader());
        dcl = new DynamicClassLoader(customCL);   
    }

    public static Class<?> compile(BundleContext bundleContext, String className, String sourceCodeInText)
            throws Exception {

        List<String> optionList = new ArrayList<String>();

        Set<String> bundleLocations = new HashSet<>();
        Bundle[] bundles = bundleContext.getBundles();
        
        getBundleLocationFor(ListServerResource.class, bundleLocations, bundles);
        getBundleLocationFor(ServerResource.class, bundleLocations, bundles);
        getBundleLocationFor(DesignerApplication.class, bundleLocations, bundles);
        getBundleLocationFor(PostDynamicEntityServerResource.class, bundleLocations, bundles);
        getBundleLocationFor(ConstraintViolation.class, bundleLocations, bundles);
        
        String locs = bundleLocations.stream().map(l -> {
            // TODO make OS specific
            return l.replace("reference:","").replace("file:/", "/").replace("%25","%"); //replace("/","\\").
        }).collect(Collectors.joining(File.pathSeparator));
        optionList.addAll(Arrays.asList("-classpath", locs));
        log.info("running javac with classpath {}", locs);

        SourceCode sourceCode = new SourceCode(className, sourceCodeInText);
        CompiledCode compiledCode = new CompiledCode(className);
        sourceCodes.add(sourceCode);
        Iterable<? extends JavaFileObject> compilationUnits = sourceCodes;//Arrays.asList(sourceCodes);

        
        
        ExtendedStandardJavaFileManager fileManager = new ExtendedStandardJavaFileManager(javac.getStandardFileManager(
                null, null, null), compiledCode, dcl);
        DiagnosticListener<? super JavaFileObject> diagnosticsListener = new DiagnosticCollector<>();
        JavaCompiler.CompilationTask task = javac.getTask(null, fileManager, diagnosticsListener, optionList, null, compilationUnits);
        task.call();
        return dcl.loadClass(className);
    }

    private static void getBundleLocationFor(Class<?> cls, Set<String> bundleLocations, Bundle[] bundles) {
        Arrays.stream(bundles).forEach(b -> {
            URL resource = b.getResource(cls.getName().replace('.', '/') + ".class");
            if (resource != null) {
                bundleLocations.add(b.getLocation());
            }
        });
    }
}