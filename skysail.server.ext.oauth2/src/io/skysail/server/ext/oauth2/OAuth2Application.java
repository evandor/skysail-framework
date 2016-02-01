package io.skysail.server.ext.oauth2;

import java.util.Arrays;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.*;
import org.osgi.service.metatype.annotations.Designate;

import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import io.skysail.domain.core.Repositories;
import io.skysail.server.app.ApiVersion;
import io.skysail.server.ext.oauth2.configuration.OAuth2Config;
import io.skysail.server.ext.oauth2.impl.OAuth2RootResource;
import io.skysail.server.menus.MenuItemProvider;

@Component(configurationPolicy = ConfigurationPolicy.REQUIRE, configurationPid = "oAuth2_")
@Designate(ocd = OAuth2Config.class, factory = true)
public class OAuth2Application extends OAuth2ApplicationGen implements ApplicationProvider, MenuItemProvider {

    private OAuth2Config oAuth2Config;

    public  OAuth2Application() {
        super("OAuth2", new ApiVersion(1), Arrays.asList());
    }

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY)
    public void setRepositories(Repositories repos) {
        super.setRepositories(repos);
    }


    @Activate
    public void activate(ComponentContext ctx) {
//        this.oAuth2Config = config;
//        System.out.println("config set to " + config);
//        setComponentContext(ctx);
    }

    @Deactivate
    public void deactivate(ComponentContext ctx) {
        setComponentContext(ctx);
        this.oAuth2Config = null;
    }
    
    public void unsetRepositories(Repositories repo) {
        super.setRepositories(null);
    }
    
    @Override
    protected void attach() {
//        OAuth2Proxy facebookProxy = new OAuth2Proxy(OAuthProviderType.FACEBOOK, oAuth2Config.getClientId(), oAuth2Config.getClientSecret());
//        facebookProxy.setRedirectUri("http://localhost:2016/OAuth2/v1/facebook");
//        facebookProxy.setNext(FacebookMeServerResource.class);
//        
//        OAuth2Proxy twitterProxy = new OAuth2Proxy(OAuthProviderType.GITHUB, oAuth2Config.getClientId(), oAuth2Config.getClientSecret());
//        twitterProxy.setRedirectUri("http://localhost:2016/OAuth2/v1/github");
//        twitterProxy.setNext(GithubServerResource.class);
        
//        router.attach(new RouteBuilder("/facebook", facebookProxy));   
//        router.attach(new RouteBuilder("/twitter", twitterProxy));   
        router.attach(new RouteBuilder("", OAuth2RootResource.class));
    }

}
