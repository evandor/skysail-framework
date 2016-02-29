package de.twenty11.skysail.server.app.tutorial.model2rest.step2;

import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public class PostTodoResource extends PostEntityServerResource<TodoModel2> {

    public PostTodoResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create Todo (Step 2)");
    }

    @Override
    public TodoModel2 createEntityTemplate() {
        return new TodoModel2();
    }

    @Override
    public void addEntity(TodoModel2 entity) {
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(PostTodoResource.class);
    }

}
