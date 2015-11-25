package io.skysail.server.designer.presentation;

import javax.annotation.Generated;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

@Generated("io.skysail.server.codegen.apt.processors.EntityProcessor")
public class PutTopicResource extends PutEntityServerResource<Topic> {

    private InteractivePresentationApplication app;
    private TopicRepo repository;

	protected void doInit() {
        super.doInit();
        app = (InteractivePresentationApplication) getApplication();
        repository = (TopicRepo) app.getRepository(Topic.class);
    }

    public Topic getEntity() {
        return repository.findOne(getAttribute("id"));
    }

    public SkysailResponse<Topic> updateEntity(Topic entity) {
        repository.update(getAttribute("id"), entity);
        return new SkysailResponse<>();
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(TopicsResource.class);
    }
}
