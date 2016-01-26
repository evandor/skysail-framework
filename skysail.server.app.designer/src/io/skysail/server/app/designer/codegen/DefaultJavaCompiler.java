package io.skysail.server.app.designer.codegen;

import org.osgi.framework.BundleContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultJavaCompiler implements JavaCompiler {

    @Override
    public boolean compile(BundleContext bundleContext) {
        try {
            InMemoryJavaCompiler.compile(bundleContext);
            return InMemoryJavaCompiler.isCompiledSuccessfully();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public Class<?> getClass(String className) throws ClassNotFoundException {
        return InMemoryJavaCompiler.getClass(className);
    }

    @Override
    public void reset() {
        InMemoryJavaCompiler.reset();
    }

    @Override
    public CompiledCode collect(String className, String entityCode) {
        return InMemoryJavaCompiler.collect(className, entityCode);
    }

    @Override
    public void collectSource(String className, String entityCode) {
        InMemoryJavaCompiler.collectSource(className, entityCode);
    }

}
