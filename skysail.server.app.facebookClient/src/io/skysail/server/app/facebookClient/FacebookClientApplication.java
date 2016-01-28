package io.skysail.server.app.facebookClient;

import java.util.Arrays;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.restlet.ext.oauth.OAuthProxy;

import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import io.skysail.domain.core.Repositories;
import io.skysail.server.app.ApiVersion;
import io.skysail.server.menus.MenuItemProvider;

@Component(immediate = true)
public class FacebookClientApplication extends FacebookClientApplicationGen implements ApplicationProvider, MenuItemProvider {

    public  FacebookClientApplication() {
        super("FacebookClient", new ApiVersion(1), Arrays.asList());
        //addToAppContext(ApplicationContextId.IMG, "/static/img/silk/page_link.png");
    }

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY)
    public void setRepositories(Repositories repos) {
        super.setRepositories(repos);
    }

    public void unsetRepositories(Repositories repo) {
        super.setRepositories(null);
    }
    
    @Override
    protected void attach() {
        super.attach();
        router.attach(new RouteBuilder("/io.skysail.server.app.facebookClient.OAuthConfigs/{id}", io.skysail.server.app.facebookClient.OAuthConfigResource.class));
        router.attach(new RouteBuilder("/io.skysail.server.app.facebookClient.OAuthConfigs/", io.skysail.server.app.facebookClient.PostOAuthConfigResource.class));
        router.attach(new RouteBuilder("/io.skysail.server.app.facebookClient.OAuthConfigs/{id}/", io.skysail.server.app.facebookClient.PutOAuthConfigResource.class));
        router.attach(new RouteBuilder("/io.skysail.server.app.facebookClient.OAuthConfigs", io.skysail.server.app.facebookClient.OAuthConfigsResource.class));
        router.attach(new RouteBuilder("", io.skysail.server.app.facebookClient.OAuthConfigsResource.class));
        
        OAuthProxy facebookProxy = new OAuthProxy(getContext());
        facebookProxy.setClientId("1481445908830165");
        facebookProxy.setClientSecret("d2991f2ae851ef74d65134e596574f88");
        facebookProxy.setRedirectURI("http://localhost:2016/Twitter/v1/facebook");
        facebookProxy.setAuthorizationURI("https://www.facebook.com/dialog/oauth");
        facebookProxy.setTokenURI("https://graph.facebook.com/oauth/access_token");
        facebookProxy.setNext(FacebookMeServerResource.class);
        router.attach(new RouteBuilder("/facebook", facebookProxy));
        
    }

}