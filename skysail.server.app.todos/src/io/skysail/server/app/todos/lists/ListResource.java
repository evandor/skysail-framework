package io.skysail.server.app.todos.lists;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.todos.*;
import io.skysail.server.app.todos.charts.ListChartResource;
import io.skysail.server.app.todos.services.ListService;
import io.skysail.server.app.todos.todos.resources.TodosResource;
import io.skysail.server.restlet.resources.EntityServerResource;

import java.util.List;

import org.restlet.resource.ClientResource;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class ListResource extends EntityServerResource<TodoList> {

    private String listId;
    private TodoApplication app;
    private ListService listService;

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
        listService = getService(ListService.class);
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        return listService.delete(this, listId);
    }

    @Override
    public TodoList getEntity() {
        return listService.getList(this, getAttribute(TodoApplication.LIST_ID));
    }

    @Override
    public TodoList getEntity(String installation) {
        if (installation == null || installation.trim().length() == 0) {
            return getEntity();
        }
        String peersCredentialsName = "Credentials_" + installation;
        String peersCredentials = getRequest().getCookies().getFirstValue(peersCredentialsName);

        String path = app.getRemotePath(installation, "/Todos/Lists/" + listId);

        if (peersCredentials == null) {
            getResponse().redirectSeeOther("/_remotelogin");
            return null;
        }
        ClientResource cr = new ClientResource(path);
        cr.getCookies().add("Credentials", peersCredentials);
        return cr.get(TodoList.class);
    }


    @Override
    public List<Link> getLinks() {
        return super.getLinks(TodosResource.class, PutListResource.class, ListChartResource.class);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(ListsResource.class);
    }

}
