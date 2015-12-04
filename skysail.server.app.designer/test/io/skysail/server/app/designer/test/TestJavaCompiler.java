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
        return TestSkysailApplication.class;
    }

    @Override
    public void reset() {
    }

    @Override
    public void collect(String className, String entityCode) throws Exception {
    }

}
