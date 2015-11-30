package io.skysail.server.app.designer;

import io.skysail.server.app.designer.codegen.SkysailEntityCompiler2;
import io.skysail.server.app.designer.model.CodegenApplicationModel;
import io.skysail.server.app.designer.model.CodegenEntityModel;
import io.skysail.server.app.designer.model.RouteModel;

import java.util.ArrayList;
import java.util.List;

public class EntityCreator {

    private CodegenApplicationModel applicationModel;

    private List<RouteModel> routeModels = new ArrayList<>();

    public EntityCreator(CodegenApplicationModel applicationModel) {
        this.applicationModel = applicationModel;
    }

    public List<RouteModel> create(STGroupBundleDir stGroup) {
        applicationModel.getEntityModels().stream().forEach(entity -> {
            // fireEvent(eventAdminRef, "compiling entity " + e.getName() +
            // " for application " + application.getName());
                routeModels.addAll(compileEntity(entity, stGroup));
            });
        return routeModels;
    }

    private List<RouteModel> compileEntity(CodegenEntityModel entityModel, STGroupBundleDir stGroup) {
        SkysailEntityCompiler2 entityCompiler = new SkysailEntityCompiler2(applicationModel, stGroup);
        entityCompiler.createEntity(entityModel);
        entityCompiler.createResources(entityModel);
        return entityCompiler.getRouteModels();
    }

}
