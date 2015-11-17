package io.skysail.server.app.designer.restclient;

import io.skysail.server.restlet.resources.ListServerResource;

import java.util.*;

import org.restlet.ext.oauth.OAuthProxy;
import org.restlet.representation.Representation;
import org.restlet.resource.*;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class ExecutionResource extends ListServerResource<Something> {

    private RestclientApplication app;
    private ClientApplicationRepo repository;
    private OAuth2Repo oAuthRepo;

    public ExecutionResource() {
        addToContext(ResourceContextId.LINK_TITLE, "execute API");
    }

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        app = (RestclientApplication)getApplication();
        repository = (ClientApplicationRepo) app.getRepository(ClientApplication.class);
        oAuthRepo = (OAuth2Repo) app.getRepository(OAuth2.class);
    }

    @Get
    public List<Something> getEntity() {
        ClientApplication ca = repository.findOne(getAttribute("id"));
        OAuth2 oauth2 = oAuthRepo.findOne(ca.getAuthentication());


        OAuthProxy oauthProxy = new OAuthProxy(getContext());
        oauthProxy.setClientId(oauth2.getClientId());
        oauthProxy.setClientSecret(oauth2.getClientSecret());
        oauthProxy.setRedirectURI("http://127.0.0.1/facebook");
        oauthProxy.setAuthorizationURI(oauth2.getAuthorizationUri());
        oauthProxy.setTokenURI(oauth2.getTokenUri());
        oauthProxy.setNext(FacebookMeServerResource.class);

        getApplication().attachToRouter("/facebook", oauthProxy);
        //router.attach("/facebook", oauthProxy);

        String uri = ca.getUrl();//.replace(":&#64;", ca.getUsername() + ":" + ca.getPassword() + "@");
        ClientResource cr = new ClientResource(uri);
        Representation representation = cr.get();
        return Arrays.asList(new Something(representation.toString()));
    }


}
