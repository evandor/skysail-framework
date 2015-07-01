package io.skysail.server.app.designer;

import io.skysail.server.app.designer.codegen.SkysailEntityCompiler2;
import io.skysail.server.app.designer.model.ApplicationModel;
import io.skysail.server.app.designer.model.EntityModel;
import io.skysail.server.app.designer.model.RouteModel;

import java.util.ArrayList;
import java.util.List;

public class EntityCreator {

    private ApplicationModel applicationModel;

    private List<RouteModel> routeModels = new ArrayList<>();

    public EntityCreator(ApplicationModel applicationModel) {
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

    private List<RouteModel> compileEntity(EntityModel entityModel, STGroupBundleDir stGroup) {
        SkysailEntityCompiler2 entityCompiler = new SkysailEntityCompiler2(applicationModel, stGroup);
        entityCompiler.createEntity(entityModel);
        entityCompiler.createResources(entityModel);
        return entityCompiler.getRouteModels();
    }

}
