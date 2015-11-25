package io.skysail.server.app.todos.lists;

import io.skysail.api.links.Link;
import io.skysail.api.text.*;
import io.skysail.server.app.todos.*;
import io.skysail.server.app.todos.services.ListService;
import io.skysail.server.app.todos.todos.resources.*;
import io.skysail.server.restlet.resources.*;
import io.skysail.server.utils.LinkUtils;

import java.util.List;

import org.restlet.data.MediaType;
import org.restlet.resource.ClientResource;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class ListsResource extends ListServerResource<TodoList> implements I18nArgumentsProvider {

    private TodoApplication app;

    private ListService listService;

    private MessageArguments messageArgs;

    public ListsResource() {
        super(ListResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "Show Todo-Lists");
        addToContext(ResourceContextId.LINK_GLYPH, "th-list");
    }

    @Override
    protected void doInit() {
        super.doInit();
        app = (TodoApplication) getApplication();
        getResourceContext().addAjaxNavigation(
                getResourceContext().getAjaxBuilder("lists-nav", "Lists:", ListsResource.class, TodosResource.class)
                        .identifier("id").createLabel("new list").createTarget(getTarget()).build());
        getResourceContext().addAjaxNavigation(
                getResourceContext()
                        .getAjaxBuilder("top10-nav", "Top 10:", Top10TodosResource.class, TodosResource.class)
                        .nameProperty("title").identifier("id").build());
        listService = getService(ListService.class);
    }

    private String getTarget() {
        Link fromResource = LinkUtils.fromResource(app, PostListResource.class);
        return fromResource != null ? fromResource.getUri() : null;
    }

    @Override
    public List<TodoList> getEntity() {
        return listService.getLists(this);
    }

    @Override
    public List<TodoList> getEntity(String installation) {
        if (installation == null || installation.trim().length() == 0) {
            return getEntity();
        }
        String peersCredentialsName = "Credentials_" + installation;
        String peersCredentials = getRequest().getCookies().getFirstValue(peersCredentialsName);

        String path = app.getRemotePath(installation, "/Todos/v2/Lists");

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
        List<Class<? extends SkysailServerResource<?>>> links = app.getMainLinks();
        links.add(PostListResource.class);
        return super.getLinks(links);
    }

    @Override
    public synchronized MessageArguments getMessageArguments() {
        if (messageArgs == null) {
            messageArgs = new MessageArguments(this.getClass())
                    .add("count of all lists of the current user", app.getListCount(getRequest()))
                    .add("count of all todos of the current user", app.getTodosCount(getRequest()))
                    .setNewIdentifier("testname").add("1", "b");
        }
        return messageArgs;
    }

}
