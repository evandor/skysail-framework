package io.skysail.server.app.designer.codegen;

import io.skysail.server.app.designer.application.Application;
import io.skysail.server.utils.BundleUtils;

import java.util.HashMap;

import org.osgi.framework.Bundle;

public class SkysailRepositoryCompiler extends SkysailCompiler {

    private Bundle bundle;
    private Application application;

    public SkysailRepositoryCompiler(Bundle bundle, Application application) {
        this.bundle = bundle;
        this.application = application;
    }
    
    public void createRepository() {
        String template = BundleUtils.readResource(bundle, "code/Repository.codegen");
        setupForCompilation(template);
    }

    private void setupForCompilation(String template) {
        @SuppressWarnings("serial")
        String entityCode = substitute(template, new HashMap<String, String>() {
            {
                put("$classname$", application.getName() + "Repository");
            }
        });
        String applicationClassName = "io.skysail.server.app.designer.gencode." + application.getName() + "Repository";
        collect(applicationClassName, entityCode);
    }
}
