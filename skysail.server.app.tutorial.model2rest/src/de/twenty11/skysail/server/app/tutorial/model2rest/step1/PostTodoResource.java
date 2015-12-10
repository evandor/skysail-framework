package de.twenty11.skysail.server.app.tutorial.model2rest.step1;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public class PostTodoResource extends PostEntityServerResource<TodoModel1> {

    public PostTodoResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create Todo (Step 1)");
    }

    @Override
    public TodoModel1 createEntityTemplate() {
        return new TodoModel1();
    }

    @Override
    public void addEntity(TodoModel1 entity) {
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(TodoResource.class);
    }

}
