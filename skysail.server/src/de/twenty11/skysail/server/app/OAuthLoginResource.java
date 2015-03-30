package de.twenty11.skysail.server.app;

import io.skysail.api.responses.SkysailResponse;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.EntityServerResource;

public class OAuthLoginResource extends EntityServerResource<String> {

    private String code;

    @Override
    protected void doInit() throws ResourceException {
        code = getQueryValue("code");
    }

    @Override
    public String getEntity() {
        // JSONObject jo = new JSONObject();
        // jo.put("client_id", System.getenv("GITHUB_CLIENT_ID"));
        // jo.put("client_secret", System.getenv("GITHUB_CLIENT_SECRET"));
        // jo.put("code", code);
        // Representation post = new
        // ClientResource("https://github.com/login/oauth/access_token").post(
        // new JsonRepresentation(jo), MediaType.APPLICATION_JSON);
        // String text = post.getText();
        // }
        return null;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

}
