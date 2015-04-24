package io.skysail.server.app.designer.codegen;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.designer.DesignerApplication;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.server.app.designer.PostDynamicEntityServerResource;

public class PostAppDesigner_Automobiles_MarkeResource extends PostDynamicEntityServerResource<AppDesigner_Automobiles_Marke> {

    public PostAppDesigner_Automobiles_MarkeResource() {
        addToContext(ResourceContextId.LINK_TITLE, "create new AppDesigner_Automobiles_Marke");
    }

    @Override
    public AppDesigner_Automobiles_Marke createEntityTemplate() {
        return new AppDesigner_Automobiles_Marke();
    }

    public SkysailResponse<?> addEntity(AppDesigner_Automobiles_Marke entity) {
        ((DesignerApplication) getApplication()).getRepository().add(entity);
        return new SkysailResponse<String>();
    }

}
