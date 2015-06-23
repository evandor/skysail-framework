package io.skysail.server.app.designer;

import io.skysail.server.app.designer.codegen.SkysailEntityCompiler2;
import io.skysail.server.app.designer.model.ApplicationModel;
import io.skysail.server.app.designer.model.EntityModel;
import io.skysail.server.app.designer.model.ReferenceModel;

import org.stringtemplate.v4.ST;

public class EntityCreator {

    private ApplicationModel applicationModel;

    public EntityCreator(ApplicationModel applicationModel) {
        this.applicationModel = applicationModel;
    }

    public void create(ST stringTemplateRoot) {
        applicationModel.getEntities().stream().forEach(entity -> {
            //fireEvent(eventAdminRef, "compiling entity " + e.getName() + " for application " + application.getName());
            compileEntity(entity,stringTemplateRoot);
        });
    }

    private void compileEntity(EntityModel entityModel, ST template) {
        
      SkysailEntityCompiler2 entityCompiler = new SkysailEntityCompiler2(applicationModel, template);
      entityCompiler.createEntity(applicationModel, entityModel);
        //      entityCompiler.createResources();
        //      entityCompiler.attachToRouter(router, a.getName(), e, routerPaths);
      for (ReferenceModel referenceModel : entityModel.getReferences()) {
          compileEntity(applicationModel.getEntityModel(referenceModel), template);
      }
    }


}
