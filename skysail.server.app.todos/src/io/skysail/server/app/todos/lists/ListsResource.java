package io.skysail.server.app.todos.lists;

import io.skysail.api.links.Link;
import io.skysail.server.app.todos.*;
import io.skysail.server.app.todos.todos.resources.*;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.server.utils.LinkUtils;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.restlet.data.MediaType;
import org.restlet.resource.ClientResource;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class ListsResource extends ListServerResource<TodoList> {

    private TodoApplication app;

    public ListsResource() {
        super(ListResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "Show Todo-Lists");
        addToContext(ResourceContextId.LINK_GLYPH, "th-list");
    }

    @Override
    protected void doInit() {
        super.doInit();
        app = (TodoApplication) getApplication();
        //getResourceContext().addAjaxNavigation("ajax", "Todo-Lists:", ListsResource.class, TodosResource.class, "lid");
        //getResourceContext().addAjaxNavigation("Top 10:", Top10TodosResource.class, TodosResource.class, "lid");
        getResourceContext().addAjaxNavigation(getResourceContext().getAjaxBuilder("lists-nav", "Lists:", ListsResource.class, TodosResource.class)
                .identifier("id")
                .createLabel("new list")
                .createTarget(getTarget())
                .build());
        getResourceContext().addAjaxNavigation(getResourceContext().getAjaxBuilder("top10-nav", "Top 10:", Top10TodosResource.class, TodosResource.class)
                .nameProperty("title")
                .identifier("id")
                .build());
    }

    private String getTarget() {
        Link fromResource = LinkUtils.fromResource(app, PostListResource.class);
        return fromResource != null ? fromResource.getUri() : null;
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
        List<Link> links = super.getLinks(PostListResource.class, PostTodoWoListResource.class, Top10TodosResource.class, PutListResource.class);
       // links.add(new Link.Builder("/Todos/docs/api").title("API").build());
        return links;
    }

}
