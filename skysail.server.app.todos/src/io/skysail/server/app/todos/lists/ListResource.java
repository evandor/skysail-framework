package io.skysail.server.app.todos.lists;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.todos.*;
import io.skysail.server.app.todos.charts.ListChartResource;
import io.skysail.server.app.todos.todos.resources.TodosResource;
import io.skysail.server.restlet.resources.EntityServerResource;

import java.util.List;

import org.restlet.data.Status;
import org.restlet.resource.ClientResource;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class ListResource extends EntityServerResource<TodoList> {

    private String listId;
    private TodoApplication app;

    public ListResource() {
        addToContext(ResourceContextId.LINK_TITLE, "details");
        addToContext(ResourceContextId.LINK_GLYPH, "search");
    }

    @Override
    protected void doInit() {
        super.doInit();
        listId = getAttribute(TodoApplication.LIST_ID);
        app = (TodoApplication) getApplication();
        getResourceContext().addAjaxNavigation(getResourceContext().getAjaxBuilder("ajax", "Lists:", ListsResource.class, TodosResource.class).identifier("id").build());
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        TodoList todoList = app.getListRepo().getById(TodoList.class, listId);
        if (todoList.getTodosCount() > 0) {
            // TODO revisit: make a business violation from that
            getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST, new IllegalStateException(),
                    "cannot delete list as it is not empty");
            return new SkysailResponse<String>();
        }
        app.getListRepo().delete(listId);
        return new SkysailResponse<String>();
    }

    @Override
    public TodoList getEntity() {
        return app.getListRepo().getById(TodoList.class, listId);
    }

    @Override
    public TodoList getEntity(String installation) {
        if (installation == null || installation.trim().length() == 0) {
            return getEntity();
        }
        String peersCredentialsName = "Credentials_" + installation;
        String peersCredentials = getRequest().getCookies().getFirstValue(peersCredentialsName);

        String path = app.getRemotePath(installation, "/Todos/Lists/" + listId);
        //String uri = path + "/Todos/Lists";

        if (peersCredentials == null) {
            getResponse().redirectSeeOther("/_remotelogin");
            return null;
        }
        ClientResource cr = new ClientResource(path);
        cr.getCookies().add("Credentials", peersCredentials);
        //cr.get(MediaType.APPLICATION_JSON);

        return cr.get(TodoList.class);
    }


    @Override
    public List<Link> getLinks() {
        // ListResource.class,
        return super.getLinks(TodosResource.class, PutListResource.class, ListChartResource.class);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(ListsResource.class);
    }

//    @Override
//    public Consumer<? super Link> getPathSubstitutions() {
//        return l -> {
//            l.substitute("lid", listId);
//        };
//    }
}
