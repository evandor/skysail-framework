package io.skysail.server.app.todos.lists;

import io.skysail.server.app.todos.TodoList;
import io.skysail.server.restlet.resources.ListServerResource;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

public class ListsClientResource extends ListServerResource<TodoList>{

    private String remoteHost;

    @Override
    protected void doInit() throws ResourceException {
        remoteHost = getAttribute("url");
    }
    
    @Override
    public List<TodoList> getEntity() {
        String uri = "http://" + remoteHost + "/Todos/Lists";
        
       
        ClientResource cr = new ClientResource("http://" + remoteHost + "/_login");
        cr.setFollowingRedirects(true);
        Form form = new Form();
        form.add("username", "admin");
        form.add("password", "23dela11_Skysail");
        cr.post(form, MediaType.TEXT_HTML);
        String credentials = cr.getResponse().getCookieSettings().getFirstValue("Credentials");
        
        cr = new ClientResource(uri);
        cr.getCookies().add("Credentials", credentials);
        cr.get(MediaType.APPLICATION_JSON);
        
        Representation representation = cr.get();
        try {
            System.out.println(representation.getText());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

}
