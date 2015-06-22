package io.skysail.server.app.designer.codegen;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.osgi.framework.BundleContext;

@Slf4j
@Getter
public class SkysailCompiler2 {

    @Getter
    private boolean compiledSuccessfully = true;

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

//        try {
//            createProjectIfNeeded();
//        } catch (IOException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        }

//        String filename = application.getPath() + "/" + application.getProjectName() + "/src/"
//                + classNameToPath(className);
//        filename = filename.replace("//", "/");
//        try {
//            Path path = Paths.get(filename);
//            new File(path.getParent().toString()).mkdirs();
//            Files.write(path, entityCode.getBytes());
//        } catch (IOException e) {
//            log.debug("could not write source code for compilation unit '{}' to '{}'", className, filename);
//        }
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

}
