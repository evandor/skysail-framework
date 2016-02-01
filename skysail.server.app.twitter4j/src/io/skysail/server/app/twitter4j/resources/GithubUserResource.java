package io.skysail.server.app.twitter4j.resources;

import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ext.oauth2.ProtectedClientResource;
import io.skysail.server.ext.oauth2.Token;
import io.skysail.server.restlet.resources.EntityServerResource;

public class GithubUserResource extends EntityServerResource<JsonEntity> {

    private static final String ENDPOINT = "https://api.github.com";
    private String url = "/users/evandor";
    
    @Override
    protected void doInit() {
        super.doInit();
        if (getQuery().getFirstValue("url") != null) {
            url = getQuery().getFirstValue("url");
        }
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

    @Override
    public JsonEntity getEntity() {
        
        System.out.println("Hier");
        
        Token token = (Token) getRequest().getAttributes().get(Token.class.getName());
        if (token == null) {
            return new JsonEntity(ENDPOINT, null);
        }
//
        ProtectedClientResource me = new ProtectedClientResource(ENDPOINT + url);
//        //me.setUseBodyMethod(true);
        me.setToken(token);
//
        try {
            Representation jsonRepresentation = me.get();
            System.out.println(me.getResponse().getHeaders());
            return new JsonEntity(ENDPOINT, new JsonRepresentation(jsonRepresentation).getJsonObject());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new JsonEntity(ENDPOINT, null);
        
    }

}
