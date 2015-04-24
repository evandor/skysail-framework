package io.skysail.server.app.designer.codegen;
                
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.api.links.Link;
                
import java.util.List;
import java.util.Map;
                
public class AppDesigner_Automobiles_MarkesResource extends ListServerResource<Map<String,Object>> {

    public List<Map<String,Object>> getEntity() {
        return ((DesignerApplication) getApplication()).getRepository().findAll("select from AppDesigner_Automobiles_Marke");
    }

    public List<Link> getLinks() {
       return super.getLinks(PostAppDesigner_Automobiles_MarkeResource.class);
    }
}
