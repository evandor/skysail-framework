package io.skysail.server.designer.presentation;

import javax.annotation.Generated;

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
    public void addEntity(Topic entity) {
        String id = repository.save(entity).toString();
        entity.setId(id);
    }

	@Override
	public String redirectTo() {
	    return super.redirectTo(TopicsResource.class);
	}
}