package de.twenty11.skysail.server.app.tutorial.model2rest.step5;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public class PostTodoResource extends PostEntityServerResource<TodoModel> {

    public PostTodoResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create Todo (Step 5)");
    }

    @Override
    public TodoModel createEntityTemplate() {
        return new TodoModel();
    }

    @Override
    public void addEntity(TodoModel entity) {
        TodosRepository.getInstance().add(entity);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(Step5DemoResource.class);
    }

}
