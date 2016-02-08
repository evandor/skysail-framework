package io.skysail.server.app.twitter4j.resources;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ext.oauth2.ProtectedClientResource;
import io.skysail.server.ext.oauth2.Token;
import io.skysail.server.restlet.resources.EntityServerResource;

public class FacebookMeServerResource extends EntityServerResource<JsonEntity> {

    private static final String ENDPOINT = "https://graph.facebook.com/me";

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

    @Override
    public JsonEntity getEntity() {
        Token token = (Token) getRequest().getAttributes().get(Token.class.getName());
        if (token == null) {
            return new JsonEntity(ENDPOINT, ENDPOINT,new JSONObject());
        }
        //
        ProtectedClientResource me = new ProtectedClientResource("https://graph.facebook.com/me");
        // me.setUseBodyMethod(true);
        me.setToken(token);

        try {
            Representation jsonRepresentation = me.get();
//            System.out.println(jsonRepresentation);
//            System.out.println(jsonRepresentation);
//            JSONObject result = new JsonRepresentation(jsonRepresentation).getJsonObject();
            return new JsonEntity(ENDPOINT, ENDPOINT, new JsonRepresentation(jsonRepresentation).getJsonObject());
        } catch (ResourceException | JSONException | IOException e) {
            e.printStackTrace();
        }

        return new JsonEntity(ENDPOINT, ENDPOINT, new JSONObject());
    }
}
