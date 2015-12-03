package io.skysail.server.app.designer.codegen;

import java.util.List;

import org.stringtemplate.v4.ST;

import io.skysail.server.app.designer.STGroupBundleDir;
import io.skysail.server.app.designer.model.*;

public class SkysailApplicationCompiler extends SkysailCompiler {

    private String applicationClassName;

    public SkysailApplicationCompiler(CodegenApplicationModel applicationModel, STGroupBundleDir stGroup, JavaCompiler compiler) {
        super(applicationModel, stGroup, compiler);
    }

    public void createApplication(List<RouteModel> routeModels) {
        ST template = getStringTemplateIndex("application");
        setupApplicationForCompilation(template, applicationModel, routeModels);
    }
   
    private String setupApplicationForCompilation(ST template, CodegenApplicationModel applicationModel, List<RouteModel> routeModels) {
        template.add("routercode", routerCode(routeModels));
        String entityCode = template.render();
        applicationClassName = applicationModel.getPackageName() + "." + applicationModel.getApplicationName() + "Application";
        collect(applicationClassName, entityCode);
        return applicationClassName;
    }

    private String routerCode(List<RouteModel> routeModels) {
        StringBuilder routerCode = new StringBuilder();
        routeModels.stream().forEach(model -> {
            routerCode.append("        router.attach(new RouteBuilder(\"").append(model.getPath()).append("\", ").append(model.getClassName()).append(".class));\n");
        });
        return routerCode.toString();
    }

    public Class<?> getApplicationClass() {
        return getClass(applicationClassName);
    }
}
