package io.skysail.server.app.designer.codegen;

import io.skysail.server.app.designer.application.Application;
import io.skysail.server.app.designer.model.ApplicationModel;
import io.skysail.server.utils.BundleUtils;

import java.util.*;

import lombok.extern.slf4j.Slf4j;

import org.osgi.framework.Bundle;

@Slf4j
public class SkysailApplicationCompiler extends SkysailCompiler {

    private Map<String, String> routerPaths;
    private String applicationClassName;

    public SkysailApplicationCompiler(Bundle bundle, Application application, Map<String, String> routerPaths) {
        super(application, bundle);
        this.routerPaths = routerPaths;
        applicationClassName = application.getPackageName() + "." + application.getName() + "Application";
    }

    public void createApplication(ApplicationModel applicationModel) {
        String template = BundleUtils.readResource(getBundle(), "code/Application.codegen");
        setupApplicationForCompilation(template, applicationModel);
    }

    private void setupApplicationForCompilation(String template, ApplicationModel applicationModel) {
        StringBuilder routerCode = new StringBuilder();
        routerPaths.keySet().stream().forEach(key -> {
            routerCode.append("        router.attach(new RouteBuilder(\"").append(key).append("\", ").append(routerPaths.get(key)).append(".class));\n");
        });
        
        //router.attach(new RouteBuilder("/Campaigns/{id}/Requests/", PostRequestResource.class));
        applicationModel.getEntities().stream().forEach(entity -> {
            entity.getReferences().stream().forEach(ref -> {
                routerCode.append("        router.attach(new RouteBuilder(\"").append("/Campaigns/{id}/Requests/").append("\", ").append("PostRequestResource").append(".class));\n");
            });
        });
        
        @SuppressWarnings("serial")
        String entityCode = substitute(template, new HashMap<String, String>() {
            {
                put("$classname$", getApplication().getName() + "Application");
                put("$appname$", getApplication().getName());
                put("$routercode$", routerCode.toString());
                put("$packagename$", getApplication().getPackageName());
            }
        });
       
        collect(applicationClassName, entityCode);
    }

    public Class<?> getApplicationClass() {
       return getClass(applicationClassName);
    }

}
