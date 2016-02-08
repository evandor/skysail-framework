package io.skysail.server.app.twitter4j.resources;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.engine.util.Base64;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ext.oauth2.ProtectedClientResource;
import io.skysail.server.ext.oauth2.Token;
import io.skysail.server.restlet.resources.EntityServerResource;

public class GithubUserResource extends EntityServerResource<JsonEntity> {

    private static final String ENDPOINT = "https://api.github.com";
    private String url = "/users/evandor/repos";
    ObjectMapper mapper = new ObjectMapper();
    
    @Override
    protected void doInit() {
        super.doInit();
        if (getQuery().getFirstValue("url") != null) {
            url = getQuery().getFirstValue("url");
        }
        if (getQuery().getFirstValue("state") != null) {
            Base64 base64 = new Base64();
            byte[] decoded = base64.decode(getQuery().getFirstValue("state"));
            String jsonWithLocation;
            try {
                jsonWithLocation = new String(decoded, "UTF-8");
                State state = mapper.readValue(jsonWithLocation, State.class);
                url = state.getLocation().substring(ENDPOINT.length());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JsonParseException e) {
                e.printStackTrace();
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

    @Override
    public JsonEntity getEntity() {

        String location = ENDPOINT + url;
        
        Token token = (Token) getRequest().getAttributes().get(Token.class.getName());
        if (token == null) {
            return new JsonEntity(ENDPOINT, location, new JSONObject());
        }
//
        ProtectedClientResource me = new ProtectedClientResource(location);
//        //me.setUseBodyMethod(true);
        me.setToken(token);
//
        Representation jsonRepresentation = me.get();
        try {
            System.out.println(me.getResponse().getHeaders());
           // System.out.println(me.getResponse().getEntityAsText());
            JSONObject jsonObject = new JsonRepresentation(jsonRepresentation).getJsonObject();
            System.out.println(jsonObject);
            return new JsonEntity(ENDPOINT, location, jsonObject);
        } catch (Exception e) {
            try {
                JSONArray jsonArray = new JsonRepresentation(jsonRepresentation).getJsonArray();
                System.out.println(jsonArray);
                return new JsonEntity(ENDPOINT, location, jsonArray);
                
            } catch (JSONException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            
            e.printStackTrace();
        }

        return new JsonEntity(ENDPOINT, location, new JSONObject());
        
    }

}
