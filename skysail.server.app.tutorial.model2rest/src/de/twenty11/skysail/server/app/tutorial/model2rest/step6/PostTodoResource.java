package de.twenty11.skysail.server.app.tutorial.model2rest.step6;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public class PostTodoResource extends PostEntityServerResource<TodoModel6> {

    public PostTodoResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create Todo (Step 5)");
    }

    @Override
    public TodoModel6 createEntityTemplate() {
        return new TodoModel6();
    }

    @Override
    public void addEntity(TodoModel6 entity) {
        Step6ModelsRepository.getInstance().add(entity);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(StepDemoResource.class);
    }

}
