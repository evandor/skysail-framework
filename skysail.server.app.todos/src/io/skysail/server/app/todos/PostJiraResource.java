package io.skysail.server.app.todos;

import java.io.IOException;

import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class PostJiraResource extends ServerResource {

    @Post("json")
    public Representation post(Representation r) {
        Representation entity = getRequestEntity();
        try {
            System.out.println(entity.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
