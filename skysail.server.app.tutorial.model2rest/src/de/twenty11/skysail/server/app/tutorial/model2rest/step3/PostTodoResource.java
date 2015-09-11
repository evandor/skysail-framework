package de.twenty11.skysail.server.app.tutorial.model2rest.step3;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PostEntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PostTodoResource extends PostEntityServerResource<TodoModel3> {

    private int newId;

    public PostTodoResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create Todo (Step 3)");
    }

    @Override
    public TodoModel3 createEntityTemplate() {
        return new TodoModel3();
    }

    @Override
    public SkysailResponse<TodoModel3> addEntity(TodoModel3 entity) {
        newId = Step3ModelsRepository.getInstance().add(entity);
        return new SkysailResponse<>();
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(Step3DemoResource.class);
    }

}
