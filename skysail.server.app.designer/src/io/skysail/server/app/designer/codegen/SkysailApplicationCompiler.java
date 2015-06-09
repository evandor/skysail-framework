package io.skysail.server.app.designer.codegen;

import io.skysail.server.app.designer.application.Application;
import io.skysail.server.utils.BundleUtils;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.osgi.framework.Bundle;

@Slf4j
public class SkysailApplicationCompiler extends SkysailCompiler {

    private Map<String, String> routerPaths;
    private String applicationClassName;

    public SkysailApplicationCompiler(Bundle bundle, Application application, Map<String, String> routerPaths) {
        super(application, bundle);
        this.routerPaths = routerPaths;
        applicationClassName = "io.skysail.server.app.designer.gencode." + application.getName() + "Application";
    }

    public void createApplication() {
        String template = BundleUtils.readResource(getBundle(), "code/Application.codegen");
        setupApplicationForCompilation(template);
    }

    private void setupApplicationForCompilation(String template) {
        StringBuilder routerCode = new StringBuilder();
        routerPaths.keySet().stream().forEach(key -> {
            routerCode.append("        router.attach(new RouteBuilder(\"").append(key).append("\", ").append(routerPaths.get(key)).append(".class));\n");
        });
        
        @SuppressWarnings("serial")
        String entityCode = substitute(template, new HashMap<String, String>() {
            {
                put("$classname$", getApplication().getName() + "Application");
                put("$appname$", getApplication().getName());
                put("$routercode$", routerCode.toString());
            }
        });
       
        collect(applicationClassName, entityCode);
    }

    public Class<?> getApplicationClass() {
       return getClass(applicationClassName);
    }

}
