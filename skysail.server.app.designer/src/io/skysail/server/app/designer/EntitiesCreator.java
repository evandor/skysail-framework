package io.skysail.server.app.designer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.skysail.server.app.designer.codegen.CompiledCode;
import io.skysail.server.app.designer.codegen.JavaCompiler;
import io.skysail.server.app.designer.codegen.SkysailEntityCompiler;
import io.skysail.server.app.designer.codegen.templates.TemplateProvider;
import io.skysail.server.app.designer.model.DesignerApplicationModel;
import io.skysail.server.app.designer.model.DesignerEntityModel;
import io.skysail.server.app.designer.model.RouteModel;
import io.skysail.server.stringtemplate.STGroupBundleDir;
import lombok.Getter;

public class EntitiesCreator {

    private DesignerApplicationModel applicationModel;

    private List<RouteModel> routeModels = new ArrayList<>();

    private JavaCompiler compiler;

    @Getter
    private Map<String, CompiledCode> code = new HashMap<>();

    private TemplateProvider templateProvider;

    public EntitiesCreator(DesignerApplicationModel applicationModel, JavaCompiler compiler, TemplateProvider templateProvider) {
        this.applicationModel = applicationModel;
        this.compiler = compiler;
        this.templateProvider = templateProvider;
    }

    public List<RouteModel> create(STGroupBundleDir stGroup) {
        applicationModel.getEntityValues().stream()
            .map(DesignerEntityModel.class::cast)
            .forEach(entity -> {
                routeModels.addAll(compileEntity(entity, stGroup, compiler));
            });
        return routeModels;
    }

    private List<RouteModel> compileEntity(DesignerEntityModel entityModel, STGroupBundleDir stGroup, JavaCompiler compiler) {
        SkysailEntityCompiler entityCompiler = new SkysailEntityCompiler(applicationModel, stGroup, compiler, templateProvider);
        CompiledCode compiledCode = entityCompiler.createEntity(entityModel);
        code.put(compiledCode.getClassName(), compiledCode);
        code.putAll(entityCompiler.createResources(entityModel));
        return entityCompiler.getRouteModels();
    }

}
