package io.skysail.server.app.designer.test;

import org.osgi.framework.BundleContext;

import io.skysail.server.app.designer.codegen.JavaCompiler;

public class TestJavaCompiler implements JavaCompiler {

    @Override
    public boolean compile(BundleContext bundleContext) {
        return true;
    }

    @Override
    public Class<?> getClass(String className) throws ClassNotFoundException {
        if (className.endsWith("DbApplication")) {
            return TestSkysailApplication.class;
        } else if (className.endsWith("Repository")) {
            return TestRepository.class;
        }
        throw new IllegalStateException();
    }

    @Override
    public void reset() {
    }

    @Override
    public void collect(String className, String entityCode) throws Exception {
    }

}
