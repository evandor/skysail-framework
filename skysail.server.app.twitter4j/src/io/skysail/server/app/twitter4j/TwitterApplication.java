package io.skysail.server.app.twitter4j;

import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.twitter4j.resources.SearchRequestResource;
import io.skysail.server.app.twitter4j.resources.TimelineEntityResource;
import io.skysail.server.app.twitter4j.resources.TimelineResource;
import io.skysail.server.app.twitter4j.resources.Tweet2TodoResource;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.ConfigurationPolicy;
import aQute.bnd.annotation.component.Deactivate;
import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.ApplicationContextId;
import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import de.twenty11.skysail.server.services.MenuItem;
import de.twenty11.skysail.server.services.MenuItemProvider;

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
        router.attach(new RouteBuilder("/searchrequest", SearchRequestResource.class));
    }

    @Override
    public List<MenuItem> getMenuEntries() {
        MenuItem appMenu = new MenuItem(APP_NAME, "/" + APP_NAME, this);
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
