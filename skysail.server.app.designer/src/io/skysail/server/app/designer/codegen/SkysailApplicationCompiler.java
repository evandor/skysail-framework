package io.skysail.server.app.designer.codegen;

import io.skysail.server.app.designer.application.Application;
import io.skysail.server.utils.BundleUtils;

import java.util.HashMap;

import lombok.extern.slf4j.Slf4j;

import org.osgi.framework.Bundle;

@Slf4j
public class SkysailApplicationCompiler extends SkysailCompiler {

    private Bundle bundle;
    private Application application;

    public SkysailApplicationCompiler(Bundle bundle, Application application) {
        this.bundle = bundle;
        this.application = application;
    }

    public void createApplication() {
        String template = BundleUtils.readResource(bundle, "code/Application.codegen");
        setupApplicationForCompilation(template);
    }

    private void setupApplicationForCompilation(String template) {
        // List<EntityField> fields = getFields(repo, appEntityName, appId);
        // String codeForFields = fields.stream().map(f -> {
        // StringBuilder sb = new StringBuilder("\n    @Field\n");
        // sb.append("    private String " + f.getName() + ";");
        // return sb.toString();
        // }).collect(Collectors.joining(";\n"));
        //
        
        StringBuilder routerCode = new StringBuilder();
        
        @SuppressWarnings("serial")
        String entityCode = substitute(template, new HashMap<String, String>() {
            {
                put("$classname$", application.getName() + "Application");
                put("$appname$", application.getName());
                put("$routercode$", routerCode.toString());
            }
        });
        String applicationClassName = "io.skysail.server.app.designer.gencode." + application.getName() + "Application";
        collect(applicationClassName, entityCode);
        // return entityClassName;

    }

}
