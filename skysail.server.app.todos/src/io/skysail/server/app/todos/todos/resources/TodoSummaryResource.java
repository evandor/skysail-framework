package io.skysail.server.app.todos.todos.resources;

import io.skysail.api.links.Link;
import io.skysail.server.app.todos.TodoApplication;
import io.skysail.server.app.todos.lists.*;
import io.skysail.server.app.todos.todos.TodoSummary;
import io.skysail.server.restlet.resources.*;
import io.skysail.server.utils.LinkUtils;

import java.util.List;

import org.restlet.resource.ResourceException;


public abstract class TodoSummaryResource extends ListServerResource<TodoSummary> {

    protected TodoApplication app;

    protected List<TodoSummary> todosSummary;

    public TodoSummaryResource(Class<? extends SkysailServerResource<?>> associatedResource) {
        super(associatedResource);
    }

    @Override
    public abstract List<TodoSummary> getEntity();

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        app = (TodoApplication) getApplication();
        getResourceContext().addAjaxNavigation(
                getResourceContext().getAjaxBuilder("lists-nav", "Lists:", ListsResource.class, TodosResource.class)
                        .createLabel("new list")
                        .createTarget(LinkUtils.fromResource(app, PostListResource.class).getUri())
                        .nameProperty("name").identifier("id").build());
    }



    @Override
    public String redirectTo() {
        if (todosSummary == null || todosSummary.isEmpty()) {
            //return super.redirectTo(PostListResource.class);
        }
        return null;
    }

    @Override
    public List<Link> getLinks() {
        List<Class<? extends SkysailServerResource<?>>> links = app.getMainLinks();
        links.add(PostTodoWoListResource.class);
        return super.getLinks(links);
    }


}
