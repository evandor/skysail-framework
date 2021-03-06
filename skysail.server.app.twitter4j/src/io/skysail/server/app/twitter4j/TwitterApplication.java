package io.skysail.server.app.twitter4j;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.oltu.oauth2.common.OAuthProviderType;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.*;
import org.osgi.service.event.EventAdmin;

import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import io.skysail.server.ApplicationContextId;
import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.twitter4j.resources.*;
import io.skysail.server.ext.oauth2.OAuth2Proxy;
import io.skysail.server.menus.MenuItem;
import io.skysail.server.menus.MenuItemProvider;
import lombok.Getter;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.REQUIRE)
public class TwitterApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    private static final String APP_NAME = "Twitter";
    private Map<String, String> config;

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    @Getter
    private volatile EventAdmin eventAdmin;
    
    @Reference
    private OAuth2Proxy oAuth2Proxy;

    public TwitterApplication() {
        super(APP_NAME);
        addToAppContext(ApplicationContextId.IMG, "/static/img/silk/page_link.png");
    }

    @Activate
    public void activate(Map<String, String> config, ComponentContext componentContext) throws ConfigurationException {
        super.activate(componentContext);
        this.config = config;
    }

    @Deactivate
    public void deactivate() {
        this.config = null;
    }

    @Override
    protected void attach() {
        super.attach();
        router.attach(new RouteBuilder("", TimelineResource.class));
        router.attach(new RouteBuilder("/tweet", TimelineEntityResource.class));
        router.attach(new RouteBuilder("/tweets/{id}", Tweet2TodoResource.class));
        
        router.attach(new RouteBuilder("/facebook", oAuth2Proxy.createProxy(OAuthProviderType.FACEBOOK, "facebook", FacebookMeServerResource.class)));
        router.attach(new RouteBuilder("/github", oAuth2Proxy.createProxy(OAuthProviderType.GITHUB, "github", GithubUserResource.class)));
    }

    @Override
    public List<MenuItem> getMenuEntries() {
        MenuItem appMenu = new MenuItem(APP_NAME, "/" + APP_NAME + getApiVersion().getVersionPath(), this);
        appMenu.setCategory(MenuItem.Category.APPLICATION_MAIN_MENU);
        return Arrays.asList(appMenu);
    }

    public Twitter getTwitterInstance() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
            .setOAuthConsumerKey(config.get("oAuthConsumerKey"))
            .setOAuthConsumerSecret(config.get("oAuthConsumerSecret"))
            .setOAuthAccessToken(config.get("oAuthAccessToken"))
            .setOAuthAccessTokenSecret(config.get("oAuthAccessTokenSecret"));
        TwitterFactory tf = new TwitterFactory(cb.build());
        return tf.getInstance();
    }

}
