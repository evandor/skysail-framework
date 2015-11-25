package io.skysail.server.designer.presentation;

import java.util.List;
import javax.annotation.Generated;

import org.restlet.resource.ResourceException;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;

@Generated("io.skysail.server.codegen.apt.processors.EntityProcessor")
public class TopicResource extends EntityServerResource<Topic> {

    private InteractivePresentationApplication app;
    private TopicRepo repository;

    protected void doInit() {
        super.doInit();
        app = (InteractivePresentationApplication)getApplication();
        repository = (TopicRepo) app.getRepository(Topic.class);
    }

    public Topic getEntity() {
        return repository.findOne(getAttribute("id"));
    }

    public List<Link> getLinks() {
        return super.getLinks(PutTopicResource.class);
    }

    public SkysailResponse<Topic> eraseEntity() {
        repository.delete(getAttribute("id"));
        return new SkysailResponse<>();
    }

    @Override
    public String redirectTo() {
        return null;//super.getLinkheader();
    }
}
