package de.twenty11.skysail.server.app;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.EntityServerResource;

public class OAuthLoginResource extends EntityServerResource<String> {

    private String code;

    @Override
    protected void doInit() throws ResourceException {
        code = getQueryValue("code");
    }

    @Override
    public String getData() {
        // JSONObject jo = new JSONObject();
        // jo.put("client_id", System.getenv("GITHUB_CLIENT_ID"));
        // jo.put("client_secret", System.getenv("GITHUB_CLIENT_SECRET"));
        // jo.put("code", code);
        // Representation post = new
        // ClientResource("https://github.com/login/oauth/access_token").post(
        // new JsonRepresentation(jo), MediaType.APPLICATION_JSON);
        // try {
        // String text = post.getText();
        // System.out.println(text);
        // } catch (IOException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
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
