package io.skysail.server.app.designer.codegen;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.osgi.framework.BundleContext;

@Slf4j
public class SkysailCompiler {
    
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
            template = template.replace(key, substitutionMap.get(key));
        }
        return template;
    }

    protected void collect(String className, String entityCode) {
        try {
            InMemoryJavaCompiler.collect(className, entityCode);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        String filename = "../skysail.server.app.designer.gencode/src/" + classNameToPath(className);
        try {
            Path path = Paths.get(filename);
            new File(path.getParent().toString()).mkdirs();
            Files.write(path, entityCode.getBytes());
        } catch (IOException e) {
            log.debug("could not write source code for compilation unit '{}' to '{}'", className, filename);
        }
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
