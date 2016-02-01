package io.skysail.server.app.twitter4j.resources;

import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import io.skysail.server.ext.oauth2.ProtectedClientResource;
import io.skysail.server.ext.oauth2.Token;

public class GithubServerResource extends ServerResource {
    
    private static final String ENDPOINT = "https://api.github.com";

    @Get
    public Representation getMe() {
       System.out.println("Hier");
        
        Token token = (Token) getRequest().getAttributes().get(Token.class.getName());
        if (token == null) {
            return new StringRepresentation("Token not found!");
        }
//
        ProtectedClientResource me = new ProtectedClientResource(ENDPOINT + "/users/evandor34");
//        //me.setUseBodyMethod(true);
        me.setToken(token);
//
        try {
            Representation jsonRepresentation = me.get();
            System.out.println(me.getResponse().getHeaders());
            JSONObject result = new JsonRepresentation(jsonRepresentation).getJsonObject();
            return new StringRepresentation("Hello! " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new StringRepresentation("Hello!");
    }
}
