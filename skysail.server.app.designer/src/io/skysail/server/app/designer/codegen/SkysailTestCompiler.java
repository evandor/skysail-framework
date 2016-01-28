package io.skysail.server.app.designer.codegen;

import java.nio.file.*;
import java.util.*;

import org.osgi.framework.Bundle;
import org.stringtemplate.v4.ST;

import io.skysail.server.app.designer.STGroupBundleDir;
import io.skysail.server.app.designer.codegen.templates.TemplateProvider;
import io.skysail.server.app.designer.model.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SkysailTestCompiler extends SkysailCompiler {

    private String applicationClassName;
    private Bundle bundle;
    private TemplateProvider templateProvider;

    public SkysailTestCompiler(DesignerApplicationModel applicationModel, STGroupBundleDir stGroup,
            Bundle bundle, JavaCompiler compiler, TemplateProvider templateProvider) {
        super(applicationModel, stGroup, compiler);
        this.bundle = bundle;
        this.templateProvider = templateProvider;
    }

    public List<CompiledCode> createTests(List<RouteModel> routeModels) {
        STGroupBundleDir stGroupBundleDir = new STGroupBundleDir(bundle, "/code/test");
        ST template = templateProvider.templateFor("abstractAppResourceTest");
        List<CompiledCode> compiledCode = setupTestsForCompilation(template, applicationModel, routeModels);
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
        } else {
            String existingCode;
        }

        //template.add("routercode", routerCode(routeModels));
        template.add("application", applicationModel);
        String entityCode = template.render();

        result.add(collectSource(applicationClassName/** + "Gen"*/, entityCode, "test-gen"));

        return result;
    }

}
