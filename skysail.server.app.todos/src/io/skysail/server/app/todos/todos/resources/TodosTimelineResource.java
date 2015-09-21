package io.skysail.server.app.todos.todos.resources;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.todos.TodoApplication;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.shiro.SecurityUtils;
import org.restlet.resource.ResourceException;

public class TodosTimelineResource extends ListServerResource<TimelineTodo> {

    private TodoApplication app;

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        app = (TodoApplication)getApplication();
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

    @Override
    public List<TimelineTodo> getEntity() {
        Filter filter = new Filter(getRequest());
        filter.add("owner", SecurityUtils.getSubject().getPrincipal().toString());

        //filter.addEdgeOut("parent", "#" + listId);

       // Pagination pagination = new Pagination(getRequest(), getResponse(), app.getTodosRepo().getTodosCount(listId,
       //         filter));
        return app.getTodosRepo().findAllTodos(filter).stream().map(t -> new TimelineTodo(t)).collect(Collectors.toList());
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(app.getMainLinks());
    }

}
