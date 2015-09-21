package io.skysail.server.app.twitter4j;

import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.twitter4j.resources.*;

import java.util.*;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import aQute.bnd.annotation.component.*;
import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.*;
import de.twenty11.skysail.server.services.*;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.require)
public class TwitterApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    private static final String APP_NAME = "Twitter";
    private Map<String, String> config;

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
