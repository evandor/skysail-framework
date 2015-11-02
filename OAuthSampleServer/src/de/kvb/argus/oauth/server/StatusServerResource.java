package de.kvb.argus.oauth.server;

import java.io.IOException;

import org.json.*;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.*;
import org.restlet.security.User;
public class StatusServerResource extends ServerResource {

    @Get("json")
    public Representation getUserStatus() throws JSONException {
        User user = getRequest().getClientInfo().getUser();
        getLogger().info("getUserStatus: " + user.getIdentifier());

        SampleUser sampleUser = OAuth2Sample.getSampleUserManager()
                .findUserById(user.getIdentifier());

        if (sampleUser == null) {
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            return null;
        }

        JSONObject result = new JSONObject();
        Object status = sampleUser.getStatus();
        if (status != null) {
            result.put("status", status);
        } else {
            result.put("status", "");
        }

        return new JsonRepresentation(result);
    }

    @Put("json")
    public Representation updateUserStatus(Representation representation)
            throws IOException, JSONException {
        JSONObject request = new JsonRepresentation(representation)
                .getJsonObject();
        Object status = request.get("status");

        if (status == null) {
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            return null;
        }

        User user = getRequest().getClientInfo().getUser();
        getLogger().info("updateUserStatus: " + user.getIdentifier());

        SampleUser sampleUser = OAuth2Sample.getSampleUserManager()
                .findUserById(user.getIdentifier());
        if (sampleUser != null) {
            sampleUser.setStatus(status.toString());
        }

        JSONObject result = new JSONObject();
        result.put("status", status);
        return new JsonRepresentation(result);
    }
}
