package de.twenty11.skysail.server.app.tutorial.model2rest.step4;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PostEntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PostTodoResource extends PostEntityServerResource<TodoModel4> {

    public PostTodoResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create Todo (Step 4)");
    }

    @Override
    public TodoModel4 createEntityTemplate() {
        return new TodoModel4();
    }

    @Override
    public SkysailResponse<TodoModel4> addEntity(TodoModel4 entity) {
        Step4ModelsRepository.getInstance().add(entity);
        return new SkysailResponse<>();
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(Step4DemoResource.class);
    }

}
