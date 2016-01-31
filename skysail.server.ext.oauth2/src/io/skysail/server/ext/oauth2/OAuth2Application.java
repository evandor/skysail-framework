package io.skysail.server.ext.oauth2;

import java.util.Arrays;

import org.apache.oltu.oauth2.common.OAuthProviderType;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.*;
import org.osgi.service.metatype.annotations.Designate;

import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import io.skysail.domain.core.Repositories;
import io.skysail.server.app.ApiVersion;
import io.skysail.server.menus.MenuItemProvider;

@Component(immediate = true,configurationPolicy = ConfigurationPolicy.REQUIRE, configurationPid = "oAuth2")
@Designate(ocd = OAuth2Config.class)
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
    public void activate(OAuth2Config config, ComponentContext ctx) {
        this.oAuth2Config = config;
        setComponentContext(ctx);
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
        OAuth2Proxy facebookProxy = new OAuth2Proxy(OAuthProviderType.FACEBOOK, oAuth2Config.clientId(), oAuth2Config.clientSecret());
        facebookProxy.setRedirectUri("http://localhost:2016/OAuth2/v1/facebook");
        facebookProxy.setNext(FacebookMeServerResource.class);
        router.attach(new RouteBuilder("/facebook", facebookProxy));   
        router.attach(new RouteBuilder("", OAuth2RootResource.class));
    }

}
