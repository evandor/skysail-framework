package io.skysail.server.app.designer.codegen;

import io.skysail.api.links.Link;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.server.utils.CompositeClassLoader;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import javax.tools.*;
import javax.tools.Diagnostic.Kind;
import javax.validation.ConstraintViolation;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.beanutils.DynaProperty;
import org.osgi.framework.*;
import org.restlet.resource.ServerResource;

@Slf4j
public class InMemoryJavaCompiler {

    private static JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
    private static DynamicClassLoader dcl;
    private static List<JavaFileObject> sourceCodes = new ArrayList<>();
    private static Map<String, CompiledCode> compiledCodes = new HashMap<>();
    private static CompiledCodeTrackingJavaFileManager fileManager = new CompiledCodeTrackingJavaFileManager(compiledCodes);

    @Getter
    private static boolean compiledSuccessfully = true;

    static {
        CompositeClassLoader customCL = new CompositeClassLoader();
        customCL.addClassLoader(Thread.currentThread().getContextClassLoader());
        customCL.addClassLoader(ListServerResource.class.getClassLoader());
        customCL.addClassLoader(DesignerApplication.class.getClassLoader());
        customCL.addClassLoader(DynaProperty.class.getClassLoader());
        customCL.addClassLoader(Link.class.getClassLoader());

        dcl = new DynamicClassLoader(customCL);
        fileManager.setClassLoader(dcl);
    }

    public static void collect(String className, String sourceCodeInText) throws Exception {

        SourceCode sourceCode = new SourceCode(className, sourceCodeInText);
        CompiledCode compiledCode = new CompiledCode(className);

        sourceCodes.add(sourceCode);
        fileManager.add(compiledCode);
    }

    public static void reset() {
        sourceCodes.clear();
        fileManager.clearCompiledCode();
    }

    public static void compile(BundleContext bundleContext)//, String className, String sourceCodeInText)
            throws Exception {

        List<String> optionList = new ArrayList<String>();

        Set<String> bundleLocations = new HashSet<>();
        Bundle[] bundles = bundleContext.getBundles();

        getBundleLocationFor(ListServerResource.class, bundleLocations, bundles);
        getBundleLocationFor(ServerResource.class, bundleLocations, bundles);
        getBundleLocationFor(DesignerApplication.class, bundleLocations, bundles);
        getBundleLocationFor(DynaProperty.class, bundleLocations, bundles);
        getBundleLocationFor(ConstraintViolation.class, bundleLocations, bundles);
        getBundleLocationFor(Link.class, bundleLocations, bundles);
        getBundleLocationFor(javax.persistence.Id.class, bundleLocations, bundles);
        getBundleLocationFor(com.fasterxml.jackson.annotation.JacksonAnnotation.class, bundleLocations, bundles);
        getBundleLocationFor(aQute.bnd.annotation.component.Component.class, bundleLocations, bundles);
        getBundleLocationFor(org.restlet.ext.raml.RamlApplication.class, bundleLocations, bundles);

        String locs = bundleLocations.stream().map(l -> {
                return l.replace("reference:", "").replace("file:/", "/").replace("%25", "%"); // replace("/","\\").
            }).collect(Collectors.joining(File.pathSeparator));
        optionList.addAll(Arrays.asList("-classpath", locs));

        sourceCodes.stream().forEach(code -> {
            log.info("about to compile {}", code);
        });
        log.info("classpath was set to {}", locs);

        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
        JavaCompiler.CompilationTask task = javac
                .getTask(null, fileManager, diagnostics, optionList, null, sourceCodes);
        task.call();
        boolean errorFound = dumpErrorsIfExistent(diagnostics, "xxx");
        if (errorFound) {
            compiledSuccessfully = false;
        }
    }

    private static boolean dumpErrorsIfExistent(DiagnosticCollector<JavaFileObject> diagnostics, String sourceCode) {
        boolean errorFound = false;
        for (Diagnostic<?> diagnostic : diagnostics.getDiagnostics()) {
            if (!(diagnostic.getKind().equals(Kind.ERROR))) {
                continue;
            }
            errorFound = true;
            log.error(diagnostic.getSource().toString());
            log.error(diagnostic.getMessage(null));
        }
        if (errorFound) {
            log.error("Non-compiling source: ");
            log.error(sourceCode);
        }
        return errorFound;
    }

    private static void getBundleLocationFor(Class<?> cls, Set<String> bundleLocations, Bundle[] bundles) {
        Arrays.stream(bundles).forEach(b -> {
            URL resource = b.getResource(cls.getName().replace('.', '/') + ".class");
            if (resource != null) {
                bundleLocations.add(b.getLocation());
            }
        });
    }

    public static Class<?> getClass(String className) throws ClassNotFoundException {
        return dcl.loadClass(className);
    }

}