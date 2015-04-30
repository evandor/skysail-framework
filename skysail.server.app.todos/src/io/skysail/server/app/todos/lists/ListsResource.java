package io.skysail.server.app.todos.lists;

import io.skysail.api.links.Link;
import io.skysail.server.app.todos.*;
import io.skysail.server.app.todos.todos.resources.PostTodoWoListResource;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;

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
        return app.getRepository().findAllLists();
    }

    @Override
    public List<TodoList> getEntity(String installation) {
        if (installation == null || installation.trim().length() == 0) {
            return getEntity();
        }
        String peersCredentialsName = "Credentials_" + installation;
        String path = app.getRemotePath(installation);
        
        String uri = path + "/Todos/Lists";
        
        String peersCredentials = getRequest().getCookies().getFirstValue(peersCredentialsName);
        
        ClientResource cr = new ClientResource(uri);

        if (peersCredentials == null) {
            
            getResponse().redirectSeeOther("/_remotelogin");
            return null;
            

//            ClientResource loginCr = new ClientResource("http://todos.int.skysail.io/_login");
//            loginCr.setFollowingRedirects(true);
//            Form form = new Form();
//            form.add("username", "admin");
//            form.add("password", "23dela11_Skysail");
//            loginCr.post(form, MediaType.TEXT_HTML);
//            String credentials = loginCr.getResponse().getCookieSettings().getFirstValue("Credentials");
//        
//            CookieSetting credentialsCookie = new CookieSetting(peersCredentialsName, credentials);
//            credentialsCookie.setAccessRestricted(true);
//            credentialsCookie.setPath("/");
//            getResponse().getCookieSettings().add(credentialsCookie);
//            cr.getCookies().add("Credentials", credentials);
        } else {
            cr.getCookies().add("Credentials", peersCredentials);
        }

        cr.get(MediaType.APPLICATION_JSON);

        List<TodoList> todoList = cr.get(List.class);
        return todoList;
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(PostListResource.class, PostTodoWoListResource.class);
    }

}
