package de.twenty11.skysail.server.app;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.EntityServerResource;

public class OAuthLoginResource extends EntityServerResource<String> {

    private String code;

    @Override
    protected void doInit() throws ResourceException {
        code = getAttribute("code");
    }

    @Override
    public String getData() {
        Map<String, String> map = new HashMap<>();
        map.put("client_id", "");
        map.put("client_secret", "");
        map.put("code", code);
        JsonRepresentation json = new JsonRepresentation(map);
        Representation post = new ClientResource("https://github.com/login/oauth/access_token").post(json);
        try {
            String text = post.getText();
            System.out.println(text);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        // TODO Auto-generated method stub
        return null;
    }

}
