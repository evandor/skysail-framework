package io.skysail.server.app.oEService;

import java.util.Collections;
import java.util.List;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.api.links.Link;
import io.skysail.server.restlet.resources.ListServerResource;

public class UsersOEsResource extends ListServerResource<OE> {

    public UsersOEsResource() {
        //super(UsersOEResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list OEs for user");
    }

    @Override
    public List<?> getEntity() {
        return Collections.emptyList();
    }
    
    @Override
    public List<Link> getLinks() {
        return super.getLinks(UsersOEsResource.class, PostUsersOERelationResource.class);
    }

}
