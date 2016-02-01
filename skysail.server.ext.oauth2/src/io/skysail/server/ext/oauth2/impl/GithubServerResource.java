package io.skysail.server.ext.oauth2.impl;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import io.skysail.server.ext.oauth2.ProtectedClientResource;
import io.skysail.server.ext.oauth2.Token;

public class GithubServerResource extends ServerResource {
    @Get
    public Representation getMe() {
       System.out.println("Hier");
        
        Token token = (Token) getRequest().getAttributes().get(Token.class.getName());
        if (token == null) {
            return new StringRepresentation("Token not found!");
        }
//
        ProtectedClientResource me = new ProtectedClientResource("https://graph.facebook.com/me");
        //me.setUseBodyMethod(true);
        me.setToken(token);

        try {
            Representation jsonRepresentation = me.get();
            System.out.println(jsonRepresentation);
            System.out.println(jsonRepresentation);
            JSONObject result = new JsonRepresentation(jsonRepresentation).getJsonObject();
            return new StringRepresentation("Hello! " + result);
        } catch (ResourceException | JSONException | IOException e) {
            e.printStackTrace();
        }

        return new StringRepresentation("Hello!");
    }
}
