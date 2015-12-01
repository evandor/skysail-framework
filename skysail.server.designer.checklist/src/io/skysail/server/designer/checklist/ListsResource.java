package io.skysail.server.designer.checklist;

import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.api.links.Link;

import java.util.List;
import java.util.Map;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class sResource extends ListServerResource<> {

    public sResource() {
        super(Resource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list s");
    }

    @Override
    public List<> getEntity() {
        return ((ChecklistApplication) getApplication()).getRepository().findAll("select from ");
    }

    public List<Link> getLinks() {
       return super.getLinks(PostResource.class);
    }
}