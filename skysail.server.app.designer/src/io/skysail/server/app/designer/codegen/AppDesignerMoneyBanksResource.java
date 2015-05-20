package io.skysail.server.app.designer.codegen;
                
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.api.links.Link;
                
import java.util.List;
import java.util.Map;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;
                
public class AppDesignerMoneyBanksResource extends ListServerResource<AppDesignerMoneyBank> {

    public AppDesignerMoneyBanksResource() {
        //super(AppDesignerMoneyBank.class);
        addToContext(ResourceContextId.LINK_TITLE, "list AppDesignerMoneyBanks");
    }
    
    public List<AppDesignerMoneyBank> getEntity() {
        return ((DesignerApplication) getApplication()).getRepository().findAll("select from AppDesignerMoneyBank");
    }

    public List<Link> getLinks() {
       return super.getLinks(PostAppDesignerMoneyBankResource.class);
    }
}
