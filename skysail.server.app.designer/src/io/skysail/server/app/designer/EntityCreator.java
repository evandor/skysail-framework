package io.skysail.server.app.designer;

import java.util.*;

import io.skysail.server.app.designer.codegen.SkysailEntityCompiler;
import io.skysail.server.app.designer.model.*;

public class EntityCreator {

    private CodegenApplicationModel applicationModel;

    private List<RouteModel> routeModels = new ArrayList<>();

    public EntityCreator(CodegenApplicationModel applicationModel) {
        this.applicationModel = applicationModel;
    }

    public List<RouteModel> create(STGroupBundleDir stGroup) {
        applicationModel.getEntityValues().stream()
            .map(CodegenEntityModel.class::cast)
            .forEach(entity -> {
            // fireEvent(eventAdminRef, "compiling entity " + e.getName() +
            // " for application " + application.getName());
                routeModels.addAll(compileEntity(entity, stGroup));
            });
        return routeModels;
    }

    private List<RouteModel> compileEntity(CodegenEntityModel entityModel, STGroupBundleDir stGroup) {
        SkysailEntityCompiler entityCompiler = new SkysailEntityCompiler(applicationModel, stGroup);
        entityCompiler.createEntity(entityModel);
        entityCompiler.createResources(entityModel);
        return entityCompiler.getRouteModels();
    }

}
