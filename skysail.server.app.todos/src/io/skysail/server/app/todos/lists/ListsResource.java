package io.skysail.server.app.todos.lists;

import io.skysail.api.links.Link;
import io.skysail.server.app.todos.*;
import io.skysail.server.app.todos.todos.resources.PostTodoWoListResource;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.server.utils.HeadersUtils;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.restlet.data.*;
import org.restlet.resource.ClientResource;
import org.restlet.util.Series;

public class ListsResource extends ListServerResource<TodoList> {

    private TodoApplication app;

    private int page = 1;

    public ListsResource() {
        super(ListResource.class);
    }

    @Override
    protected void doInit() {
        app = (TodoApplication) getApplication();
    }

    @Override
    public List<TodoList> getEntity() {
        // TODO code doublication with TodosResource, come up with a generic scheme
        int linesPerPage = 10;
        String username = SecurityUtils.getSubject().getPrincipal().toString();

        Series<Header> headers = HeadersUtils.getHeaders(getResponse());
        long clipCount = app.getRepository().getListsCount(username);
        headers.add(new Header(HeadersUtils.PAGINATION_PAGES, Long.toString(1 + Math.floorDiv(clipCount, linesPerPage))));
        headers.add(new Header(HeadersUtils.PAGINATION_PAGE, Integer.toString(page )));
        headers.add(new Header(HeadersUtils.PAGINATION_HITS, Long.toString(clipCount)));

        return app.getRepository().findAllLists();
    }

    @Override
    public List<TodoList> getEntity(String installation) {
        if (installation == null || installation.trim().length() == 0) {
            return getEntity();
        }
        String peersCredentialsName = "Credentials_" + installation;
        String peersCredentials = getRequest().getCookies().getFirstValue(peersCredentialsName);

        String path = app.getRemotePath(installation, "/Todos/Lists");
        //String uri = path + "/Todos/Lists";
        
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
        return super.getLinks(PostListResource.class, PostTodoWoListResource.class);
    }

}
