package de.twenty11.skysail.server.app.tutorial.model2rest.step3;

import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.resources.PostEntityServerResource;

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
    public void addEntity(TodoModel3 entity) {
        newId = Step3ModelsRepository.getInstance().add(entity);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(Step3DemoResource.class);
    }

}
