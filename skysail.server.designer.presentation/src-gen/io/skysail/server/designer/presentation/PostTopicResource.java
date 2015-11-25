package io.skysail.server.designer.presentation;

import javax.annotation.Generated;

//

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PostEntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

@Generated("io.skysail.server.codegen.apt.processors.EntityProcessor")
public class PostTopicResource extends PostEntityServerResource<Topic> {

	private InteractivePresentationApplication app;
    private TopicRepo repository;

	public PostTopicResource() {
	    addToContext(ResourceContextId.LINK_TITLE, "Create new Topic");
    }

    @Override
	protected void doInit() {
		app = (InteractivePresentationApplication)getApplication();
        repository = (TopicRepo) app.getRepository(Topic.class);
	}

	@Override
    public Topic createEntityTemplate() {
	    return new Topic();
    }

    @Override
    public SkysailResponse<Topic> addEntity(Topic entity) {
        String id = repository.save(entity).toString();
        entity.setId(id);
        return new SkysailResponse<>(entity);
    }

	@Override
	public String redirectTo() {
	    return super.redirectTo(TopicsResource.class);
	}
}