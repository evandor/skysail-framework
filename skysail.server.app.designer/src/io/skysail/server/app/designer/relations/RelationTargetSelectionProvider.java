package io.skysail.server.app.designer.relations;

import java.util.LinkedHashMap;
import java.util.Map;

import org.restlet.resource.Resource;

import io.skysail.domain.html.SelectionProvider;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.entities.DbEntity;

public class RelationTargetSelectionProvider implements SelectionProvider {

    public static RelationTargetSelectionProvider getInstance() {
        return new RelationTargetSelectionProvider();
    }

    private Resource resource;

    @Override
    public Map<String, String> getSelections() {
        Map<String,String> result = new LinkedHashMap<>();
        DesignerApplication app = (DesignerApplication) resource.getApplication();
        DbEntity entity = app.getRepository().findEntity(resource.getAttribute("eid"));
        entity.getDbApplication().getEntities().stream().forEach(e -> {
            result.put(e.getId(), e.getName());
        });
        return result;
    }

    @Override
    public void setConfiguration(Object osgiServicesProvider) {
    }

    @Override
    public void setResource(Resource resource) {
        this.resource = resource;
    }

}
