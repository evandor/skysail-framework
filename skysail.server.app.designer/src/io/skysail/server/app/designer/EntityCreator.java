package io.skysail.server.app.designer;

import java.util.*;

import io.skysail.server.app.designer.codegen.*;
import io.skysail.server.app.designer.model.*;

public class EntityCreator {

    private CodegenApplicationModel applicationModel;

    private List<RouteModel> routeModels = new ArrayList<>();

    private JavaCompiler compiler;

    public EntityCreator(CodegenApplicationModel applicationModel, JavaCompiler compiler) {
        this.applicationModel = applicationModel;
        this.compiler = compiler;
    }

    public List<RouteModel> create(STGroupBundleDir stGroup) {
        applicationModel.getEntityValues().stream()
            .map(CodegenEntityModel.class::cast)
            .forEach(entity -> {
            // fireEvent(eventAdminRef, "compiling entity " + e.getName() +
            // " for application " + application.getName());
                routeModels.addAll(compileEntity(entity, stGroup, compiler));
            });
        return routeModels;
    }

    private List<RouteModel> compileEntity(CodegenEntityModel entityModel, STGroupBundleDir stGroup, JavaCompiler compiler) {
        SkysailEntityCompiler entityCompiler = new SkysailEntityCompiler(applicationModel, stGroup, compiler);
        entityCompiler.createEntity(entityModel);
        entityCompiler.createResources(entityModel);
        return entityCompiler.getRouteModels();
    }

}
