package io.skysail.server.app.designer.codegen;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.osgi.framework.Bundle;
import org.stringtemplate.v4.ST;

import io.skysail.server.app.designer.codegen.templates.TemplateProvider;
import io.skysail.server.app.designer.codegen.writer.ProjectFileWriter;
import io.skysail.server.app.designer.model.DesignerApplicationModel;
import io.skysail.server.app.designer.model.RouteModel;
import io.skysail.server.stringtemplate.STGroupBundleDir;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SkysailApplicationCompiler extends SkysailCompiler {

    private String applicationClassName;
    private Bundle bundle;
    private TemplateProvider templateProvider;

    public SkysailApplicationCompiler(DesignerApplicationModel applicationModel, STGroupBundleDir stGroup,
            Bundle bundle, JavaCompiler compiler, TemplateProvider templateProvider) {
        super(applicationModel, stGroup, compiler);
        this.bundle = bundle;
        this.templateProvider = templateProvider;
    }

    public List<CompiledCode> createApplication(List<RouteModel> routeModels) {
        ST template =  templateProvider.templateFor("application.stg");
        List<CompiledCode> compiledCode = setupApplicationForCompilation(template, applicationModel, routeModels);

        ST dsTemplate = templateProvider.templateFor("OSGI-INF/applicationXml");
        String xml = dsTemplate.render();
        ProjectFileWriter.save(applicationModel, "bundle/OSGI-INF",
                applicationModel.getPackageName() + "." + applicationModel.getName() + "Application.xml",
                xml.getBytes());

        return compiledCode;
    }

    private List<CompiledCode> setupApplicationForCompilation(ST template, DesignerApplicationModel applicationModel,
            List<RouteModel> routeModels) {

        List<CompiledCode> result = new ArrayList<>();
        applicationClassName = applicationModel.getPackageName() + "." + applicationModel.getName() + "Application";

        String applicationClassNameInSourceFolder = applicationModel.getPath() + "/" + applicationModel.getProjectName()
                + "/src/" + classNameToPath(applicationClassName);
        applicationClassNameInSourceFolder = applicationClassNameInSourceFolder.replace("//", "/");

        Path path = Paths.get(applicationClassNameInSourceFolder);
        if (!path.toFile().exists()) {
            ST applicationExtendedtemplate = templateProvider.templateFor("applicationExtended");
            result.add(
                    setupExtendedApplicationForCompilation(applicationExtendedtemplate, applicationModel, routeModels));
        } else {
            String existingCode;
            try {
                existingCode = Files.readAllLines(path).stream().collect(Collectors.joining("\n"));
                result.add(collect(applicationClassName, existingCode, "src"));
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }

        template.add("routercode", routerCode(routeModels));
        String entityCode = template.render();

        result.add(collect(applicationClassName + "Gen", entityCode, "src-gen"));

        return result;
    }

    private CompiledCode setupExtendedApplicationForCompilation(ST template, DesignerApplicationModel applicationModel,
            List<RouteModel> routeModels) {
        applicationClassName = applicationModel.getPackageName() + "." + applicationModel.getName() + "Application";

        String applicationClassNameInSourceFolder = applicationModel.getPath() + "/" + applicationModel.getProjectName()
                + "/src/" + classNameToPath(applicationClassName);
        applicationClassNameInSourceFolder = applicationClassNameInSourceFolder.replace("//", "/");

        template.add("routercode", routerCode(routeModels));
        String entityCode = template.render();
        return collect(applicationClassName, entityCode, "src");
    }

    private String routerCode(List<RouteModel> routeModels) {
        StringBuilder routerCode = new StringBuilder();
        routeModels.stream().forEach(routeModel -> {
            routerCode.append("        router.attach(new RouteBuilder(\"").append(routeModel.getPath()).append("\", ")
                    .append(routeModel.getClassName()).append(".class));\n");
        });
        return routerCode.toString();
    }

    public Class<?> getApplicationClass() {
        return getClass(applicationClassName);
    }
}
