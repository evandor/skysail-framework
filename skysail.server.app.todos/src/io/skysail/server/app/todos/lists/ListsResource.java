package io.skysail.server.app.todos.lists;

import io.skysail.api.links.Link;
import io.skysail.server.app.todos.TodoApplication;
import io.skysail.server.app.todos.TodoList;
import io.skysail.server.app.todos.todos.resources.PostTodoWoListResource;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.restlet.data.MediaType;
import org.restlet.resource.ClientResource;

public class ListsResource extends ListServerResource<TodoList> {

    private TodoApplication app;

    public ListsResource() {
        super(ListResource.class);
    }

    @Override
    protected void doInit() {
        app = (TodoApplication) getApplication();
    }

    @Override
    public List<TodoList> getEntity() {
        Filter filter = new Filter(getRequest());
        filter.add("owner", SecurityUtils.getSubject().getPrincipal().toString());
        Pagination pagination = new Pagination(getRequest(), getResponse(), app.getRepository().getListsCount(filter));
        return app.getRepository().findAllLists(filter, pagination);
    }

    @Override
    public List<TodoList> getEntity(String installation) {
        if (installation == null || installation.trim().length() == 0) {
            return getEntity();
        }
        String peersCredentialsName = "Credentials_" + installation;
        String peersCredentials = getRequest().getCookies().getFirstValue(peersCredentialsName);

        String path = app.getRemotePath(installation, "/Todos/Lists");
        // String uri = path + "/Todos/Lists";

        if (peersCredentials == null) {
            getResponse().redirectSeeOther("/_remotelogin");
            return null;
        }
        ClientResource cr = new ClientResource(path);
        cr.getCookies().add("Credentials", peersCredentials);
        cr.get(MediaType.APPLICATION_JSON);

        return cr.get(List.class);
    }

    @Override
    public List<Link> getLinks() {
        List<Link> links = super.getLinks(PostListResource.class, PostTodoWoListResource.class);
       // links.add(new Link.Builder("/Todos/docs/api").title("API").build());
        return links;
    }

}
