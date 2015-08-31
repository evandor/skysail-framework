package de.twenty11.skysail.server.app.tutorial.model2rest.step6;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PostEntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PostTodoResource extends PostEntityServerResource<TodoModel6> {

    public PostTodoResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create Todo (Step 5)");
    }

    @Override
    public TodoModel6 createEntityTemplate() {
        return new TodoModel6();
    }

    @Override
    public SkysailResponse<?> addEntity(TodoModel6 entity) {
        Step6ModelsRepository.getInstance().add(entity);
        return new SkysailResponse<String>();
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(StepDemoResource.class);
    }

}
