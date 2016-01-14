package io.skysail.server.app.designer.codegen;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.stringtemplate.v4.ST;

import io.skysail.server.app.designer.STGroupBundleDir;
import io.skysail.server.app.designer.model.DesignerApplicationModel;
import io.skysail.server.app.designer.model.RouteModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SkysailApplicationCompiler extends SkysailCompiler {

    private String applicationClassName;

    public SkysailApplicationCompiler(DesignerApplicationModel applicationModel, STGroupBundleDir stGroup,
            JavaCompiler compiler) {
        super(applicationModel, stGroup, compiler);
    }

    public void createApplication(List<RouteModel> routeModels) {
        ST template = getStringTemplateIndex("application");
        setupApplicationForCompilation(template, applicationModel, routeModels);
    }

    private String setupApplicationForCompilation(ST template, DesignerApplicationModel applicationModel,
            List<RouteModel> routeModels) {
        applicationClassName = applicationModel.getPackageName() + "." + applicationModel.getName() + "Application";

        String applicationClassNameInSourceFolder = applicationModel.getPath() + "/" + applicationModel.getProjectName()
                + "/src/" + classNameToPath(applicationClassName);
        applicationClassNameInSourceFolder = applicationClassNameInSourceFolder.replace("//", "/");

        Path path = Paths.get(applicationClassNameInSourceFolder);
        if (!path.toFile().exists()) {
            ST applicationExtendedtemplate = getStringTemplateIndex("applicationExtended");
            setupExtendedApplicationForCompilation(applicationExtendedtemplate, applicationModel, routeModels);
        }

        template.add("routercode", routerCode(routeModels));
        String entityCode = template.render();
        applicationClassName += "Gen";
        collect(applicationClassName, entityCode);
        return applicationClassName;
    }

    private String setupExtendedApplicationForCompilation(ST template, DesignerApplicationModel applicationModel,
            List<RouteModel> routeModels) {
        applicationClassName = applicationModel.getPackageName() + "." + applicationModel.getName() + "Application";

        String applicationClassNameInSourceFolder = applicationModel.getPath() + "/" + applicationModel.getProjectName()
                + "/src/" + classNameToPath(applicationClassName);
        applicationClassNameInSourceFolder = applicationClassNameInSourceFolder.replace("//", "/");

        template.add("routercode", routerCode(routeModels));
        String entityCode = template.render();
        collect(applicationClassName, entityCode);
        return applicationClassName;
    }

    private String routerCode(List<RouteModel> routeModels) {
        StringBuilder routerCode = new StringBuilder();
        routeModels.stream().forEach(model -> {
            routerCode.append("        router.attach(new RouteBuilder(\"").append(model.getPath()).append("\", ")
                    .append(model.getClassName()).append(".class));\n");
        });
        return routerCode.toString();
    }

    public Class<?> getApplicationClass() {
        return getClass(applicationClassName);
    }
}
