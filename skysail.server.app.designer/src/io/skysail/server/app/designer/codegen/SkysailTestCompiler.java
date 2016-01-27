package io.skysail.server.app.designer.codegen;

import java.nio.file.*;
import java.util.*;

import org.osgi.framework.Bundle;
import org.stringtemplate.v4.ST;

import io.skysail.server.app.designer.STGroupBundleDir;
import io.skysail.server.app.designer.model.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SkysailTestCompiler extends SkysailCompiler {

    private String applicationClassName;
    private Bundle bundle;

    public SkysailTestCompiler(DesignerApplicationModel applicationModel, STGroupBundleDir stGroup,
            Bundle bundle, JavaCompiler compiler) {
        super(applicationModel, stGroup, compiler);
        this.bundle = bundle;
    }

    public List<CompiledCode> createTests(List<RouteModel> routeModels) {
        STGroupBundleDir stGroupBundleDir = new STGroupBundleDir(bundle, "/code/test");
        ST template = getStringTemplateIndex(stGroupBundleDir, "abstractAppResourceTest");
        List<CompiledCode> compiledCode = setupTestsForCompilation(template, applicationModel, routeModels);
//
//        STGroupBundleDir stGroupBundleDir = new STGroupBundleDir(bundle, "/code/OSGI-INF");
//        ST dsTemplate = getStringTemplateIndex(stGroupBundleDir, "applicationXml");
//        dsTemplate.add("model", applicationModel);
//        String xml = dsTemplate.render();
//        ProjectFileWriter.save(applicationModel, "bundle/OSGI-INF",
//                applicationModel.getPackageName() + "." + applicationModel.getName() + "Application.xml",
//                xml.getBytes());
        return compiledCode;
    }

    private List<CompiledCode> setupTestsForCompilation(ST template, DesignerApplicationModel applicationModel,
            List<RouteModel> routeModels) {

        List<CompiledCode> result = new ArrayList<>();
        applicationClassName = applicationModel.getPackageName() + ".Abstract" + applicationModel.getName() + "ResourceTest";

        String applicationClassNameInSourceFolder = applicationModel.getPath() + "/" + applicationModel.getProjectName()
                + "/test/" + classNameToPath(applicationClassName);
        applicationClassNameInSourceFolder = applicationClassNameInSourceFolder.replace("//", "/");

        Path path = Paths.get(applicationClassNameInSourceFolder);
        if (!path.toFile().exists()) {
            //ST applicationExtendedtemplate = getStringTemplateIndex("applicationExtended");
            //result.add(
            //        setupExtendedApplicationForCompilation(applicationExtendedtemplate, applicationModel, routeModels));
        } else {
            String existingCode;
//            try {
//                existingCode = Files.readAllLines(path).stream().collect(Collectors.joining("\n"));
//                result.add(collect(applicationClassName, existingCode, "src"));
//            } catch (IOException e) {
//                log.error(e.getMessage(), e);
//            }
        }

        //template.add("routercode", routerCode(routeModels));
        template.add("application", applicationModel);
        String entityCode = template.render();

        result.add(collectSource(applicationClassName/** + "Gen"*/, entityCode, "test-gen"));

        return result;
    }
//
//    private CompiledCode setupExtendedApplicationForCompilation(ST template, DesignerApplicationModel applicationModel,
//            List<RouteModel> routeModels) {
//        applicationClassName = applicationModel.getPackageName() + "." + applicationModel.getName() + "Application";
//
//        String applicationClassNameInSourceFolder = applicationModel.getPath() + "/" + applicationModel.getProjectName()
//                + "/src/" + classNameToPath(applicationClassName);
//        applicationClassNameInSourceFolder = applicationClassNameInSourceFolder.replace("//", "/");
//
//        template.add("routercode", routerCode(routeModels));
//        String entityCode = template.render();
//        return collect(applicationClassName, entityCode, "src");
//    }
//
//    private String routerCode(List<RouteModel> routeModels) {
//        StringBuilder routerCode = new StringBuilder();
//        routeModels.stream().forEach(model -> {
//            routerCode.append("        router.attach(new RouteBuilder(\"").append(model.getPath()).append("\", ")
//                    .append(model.getClassName()).append(".class));\n");
//        });
//        return routerCode.toString();
//    }
//
//    public Class<?> getApplicationClass() {
//        return getClass(applicationClassName);
//    }
}