package io.skysail.server.ext.oauth2;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import lombok.Setter;

public class ProtectedClientResource extends ClientResource {

    private static final String ACCESS_TOKEN = "access_token";

    @Setter
    private Token token;

    public ProtectedClientResource(String uri) {
        super(uri);
    }

    @Override
    public Response handleOutbound(Request request) {
        if (token == null) {
            throw new ResourceException(Status.CLIENT_ERROR_UNAUTHORIZED, "Token not found");
        }
        // if (token.getTokenType().equals(TOKEN_TYPE_BEARER)) {
        // if (isUseBodyMethod()) {
        Representation entity = request.getEntity();
        if (entity != null && entity.getMediaType().equals(MediaType.APPLICATION_WWW_FORM)) {
            Form form = new Form(entity);
            form.add(ACCESS_TOKEN, token.getAccessToken());
            request.setEntity(form.getWebRepresentation());
        } else {
            request.getResourceRef().addQueryParameter(ACCESS_TOKEN, token.getAccessToken());
        }
        // } else {
        // ChallengeResponse cr = new ChallengeResponse(
        // ChallengeScheme.HTTP_OAUTH_BEARER);
        // cr.setRawValue(token.getAccessToken());
        // request.setChallengeResponse(cr);
        // }
        // } else {
        // throw new ResourceException(Status.CLIENT_ERROR_UNAUTHORIZED,
        // "Unsupported token type.");
        return super.handleOutbound(request);
    }

}
