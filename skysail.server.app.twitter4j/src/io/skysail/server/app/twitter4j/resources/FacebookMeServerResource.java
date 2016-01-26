package io.skysail.server.app.twitter4j.resources;

import java.io.IOException;

import org.json.*;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.ext.oauth.ProtectedClientResource;
import org.restlet.ext.oauth.internal.Token;
import org.restlet.representation.*;
import org.restlet.resource.*;

public class FacebookMeServerResource extends ServerResource {
    @Get
    public Representation getMe() throws IOException, JSONException {
        Token token = (Token) getRequest().getAttributes().get(Token.class.getName());
        if (token == null) {
            return new StringRepresentation("Token not found!");
        }

        ProtectedClientResource me = new ProtectedClientResource("https://graph.facebook.com/me");
        me.setUseBodyMethod(true);
        me.setToken(token);

        JSONObject result = new JsonRepresentation(me.get()).getJsonObject();

        return new StringRepresentation("Hello " + result.getString("name") + "<br><br>" + result.toString());
    }
}
