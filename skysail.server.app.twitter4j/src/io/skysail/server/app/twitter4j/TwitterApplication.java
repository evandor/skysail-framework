package io.skysail.server.app.twitter4j;

import java.util.*;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.*;
import org.osgi.service.event.EventAdmin;
import org.restlet.ext.oauth.OAuthProxy;

import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.*;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.twitter4j.resources.*;
import io.skysail.server.menus.*;
import lombok.Getter;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.REQUIRE)
public class TwitterApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    private static final String APP_NAME = "Twitter";
    private Map<String, String> config;

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    @Getter
    private volatile EventAdmin eventAdmin;

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
        //        router.attach(new RouteBuilder("/searchrequest", SearchRequestResource.class));
        
//        OAuthProxy twitterProxy = new OAuthProxy(getContext(), false);
//        twitterProxy.setClientId("");
//        twitterProxy.setClientSecret("");
//        twitterProxy.setRedirectURI("http://127.0.0.1:8888/twitter/");
//        twitterProxy.setAuthorizationURI("https://api.twitter.com/oauth/authorize");
//        twitterProxy.setTokenURI("https://api.twitter.com/oauth/request_token");
//        twitterProxy.setScope(new String[] { "https://www.google.com/m8/feeds/" });
//        twitterProxy.setNext(TwitterServerResource.class);
        
        
        OAuthProxy facebookProxy = new OAuthProxy(getContext());
        facebookProxy.setClientId("");
        facebookProxy.setClientSecret("");
        facebookProxy.setRedirectURI("http://localhost:2016/Twitter/v1/facebook");
        facebookProxy.setAuthorizationURI("https://www.facebook.com/dialog/oauth");
        facebookProxy.setTokenURI("https://graph.facebook.com/oauth/access_token");
        facebookProxy.setNext(FacebookMeServerResource.class);
        router.attach(new RouteBuilder("/facebook", facebookProxy));
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
