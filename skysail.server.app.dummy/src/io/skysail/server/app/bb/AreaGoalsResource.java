package io.skysail.server.app.bb;

import java.util.Arrays;
import java.util.List;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.bb.areas.Area;
import io.skysail.server.app.bb.goals.wc.PostWorkAndCareerGoalResource;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.utils.LinkUtils;

public abstract class AreaGoalsResource extends ListServerResource<DummyGoal> {

    protected BBApplication app;

    protected Area area;

    public AreaGoalsResource(Class<? extends SkysailServerResource<?>> associatedResource, Area area) {
        super(associatedResource);
        this.area = area; 
        addToContext(ResourceContextId.LINK_TITLE, area.toString());
    }

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        app = (BBApplication) getApplication();
    }
    
    @Override
    public List<DummyGoal> getEntity() {
        Filter filter = new Filter(getRequest());
        filter.add("area", area.name());
        return app.getRepository().find(filter);
    }
    
    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }
    
    @Override
    public List<Link> getLinks() {
        return super.getLinks(app.getMainLinks());
    }
    
    @SafeVarargs
    protected
    final List<Class<? extends SkysailServerResource<?>>> mainLinksPlus(Class<? extends SkysailServerResource<?>>... classes) {
        List<Class<? extends SkysailServerResource<?>>> mainLinks = app.getMainLinks();
        Arrays.stream(classes).forEach(c -> mainLinks.add(c));
        return mainLinks;
    }


}
