package de.twenty11.skysail.server.app.tutorial.model2rest.home;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.resources.EntityServerResource;

import java.util.List;

import de.twenty11.skysail.server.app.tutorial.model2rest.*;

public class TutorialHomeResource extends EntityServerResource<Dummy> {

    public TutorialHomeResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Getting Started");
    }

    @Override
    public Dummy getEntity() {
        return new Dummy();
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

    @Override
    public List<Link> getLinks() {
         List<Link> linkheader = ((Model2RestTutorialApplication) getApplication()).getAppNavigation(this);
         //linkheader.add(new Link.Builder("/tutorialM2R/step1").title("Start tutorial: Create Model").build());
         return linkheader;
    }

}
