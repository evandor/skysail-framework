package io.skysail.server.app.designer.codegen;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

import org.stringtemplate.v4.ST;

import io.skysail.server.app.designer.STGroupBundleDir;
import io.skysail.server.app.designer.model.*;
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
        } else {
            String existingCode;
            try {
                existingCode = Files.readAllLines(path).stream().collect(Collectors.joining("\n"));
                collect(applicationClassName, existingCode,"src");
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }

        template.add("routercode", routerCode(routeModels));
        String entityCode = template.render();
        
        collect(applicationClassName + "Gen", entityCode, "src-gen");
        return applicationClassName + "Gen";
    }

    private String setupExtendedApplicationForCompilation(ST template, DesignerApplicationModel applicationModel,
            List<RouteModel> routeModels) {
        applicationClassName = applicationModel.getPackageName() + "." + applicationModel.getName() + "Application";

        String applicationClassNameInSourceFolder = applicationModel.getPath() + "/" + applicationModel.getProjectName()
                + "/src/" + classNameToPath(applicationClassName);
        applicationClassNameInSourceFolder = applicationClassNameInSourceFolder.replace("//", "/");

        template.add("routercode", routerCode(routeModels));
        String entityCode = template.render();
        collect(applicationClassName, entityCode,"src");
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
