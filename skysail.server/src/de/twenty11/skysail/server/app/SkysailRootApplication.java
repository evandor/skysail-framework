package de.twenty11.skysail.server.app;

import java.util.Dictionary;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.shiro.SecurityUtils;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.component.ComponentContext;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;
import de.twenty11.skysail.server.app.profile.ProfileResource;
import de.twenty11.skysail.server.app.profile.PutPasswordResource;
import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import de.twenty11.skysail.server.help.HelpEntry;
import de.twenty11.skysail.server.help.HelpTour;
import de.twenty11.skysail.server.resources.AboutResource;
import de.twenty11.skysail.server.resources.DefaultResource;
import de.twenty11.skysail.server.resources.LoginResource;
import de.twenty11.skysail.server.resources.NameResource;
import de.twenty11.skysail.server.resources.VersionResource;
import de.twenty11.skysail.server.resources.WelcomeResource;
import de.twenty11.skysail.server.services.MenuItem;
import de.twenty11.skysail.server.services.MenuItemProvider;
import de.twenty11.skysail.server.services.ResourceBundleProvider;
import de.twenty11.skysail.server.services.UserManager;

@Component(immediate = true, properties = { "service.pid=landingpages" })
public class SkysailRootApplication extends SkysailApplication implements ApplicationProvider, ResourceBundleProvider,
        ManagedService {

    public static final String ROOT_APPLICATION_NAME = "root";

    public static final String LOGIN_PATH = "/_login";
    public static final String LOGOUT_PATH = "/_logout";
    public static final String PROFILE_PATH = "/_profile";
    public static final String VERSION_PATH = "/_version";
    public static final String NAME_PATH = "/_name";
    public static final String ABOUT_PATH = "/_about";
    public static final String LARGETESTS_PATH = "/_largetests";
    public static final String WEBCONSOLE_PATH = "/webconsole";
    public static final String WELCOME_PATH = "/welcome";

    private volatile Set<SkysailApplication> applications = new TreeSet<>();

    private volatile Set<MenuItemProvider> menuProviders = new HashSet<>();

    private UserManager userManager;

    private Dictionary<String, ?> properties;

    public SkysailRootApplication() {
        super(ROOT_APPLICATION_NAME);
    }

    @Override
    @Activate
    protected synchronized void activate(ComponentContext componentContext) {
        if (getContext() != null) {
            setContext(getContext().createChildContext());
        }
        setComponentContext(componentContext);
    }

    @Override
    @Deactivate
    protected synchronized void deactivate(ComponentContext componentContext) {
        setComponentContext(null);
    }

    @Override
    protected void attach() {
        router.attach(new RouteBuilder("/", DefaultResource.class).noAuthenticationNeeded());
        // see ShiroDelegationAuthenticator
        router.attach(new RouteBuilder(LOGIN_PATH, LoginResource.class).noAuthenticationNeeded());
        router.attach(new RouteBuilder(LOGIN_PATH + "/{provider}", OAuthLoginResource.class).noAuthenticationNeeded());
        router.attach(new RouteBuilder(VERSION_PATH, VersionResource.class));
        router.attach(new RouteBuilder(NAME_PATH, NameResource.class));
        router.attach(new RouteBuilder(ABOUT_PATH, AboutResource.class).noAuthenticationNeeded());
        router.attach(new RouteBuilder(PROFILE_PATH, ProfileResource.class));
        router.attach(new RouteBuilder(PROFILE_PATH + "/password/", PutPasswordResource.class));
        router.attach(new RouteBuilder(LARGETESTS_PATH, LargeTestsResource.class));
        router.attach(new RouteBuilder(LARGETESTS_PATH + "/{id}", LargeTestsFileResource.class));
        router.attach(new RouteBuilder(WELCOME_PATH, WelcomeResource.class));
        router.attach(new RouteBuilder("/_iframe", IFrameResource.class));
    }

    public Set<SkysailApplication> getApplications() {
        return this.applications;
    }

    @Override
    public HelpTour getHelpTour() {
        HelpTour helpTour = new HelpTour("joyRideTipContent");

        HelpEntry entry = helpTour.addEntry().withId("skysailProductName").andText("Next").andCssClass("custom");
        entry.addText("<h2>Welcome!</h2>");
        entry.addText("<p>You found this page, so you are at the skysail notes <i>backend</i>!</p>");
        entry.addText("<p>Great to see you ;)</p>");
        entry.addText("<p>skysail <b>notes</b> is a webapplication dealing with, you guessed, <i>notes</i>.</p>");

        entry = helpTour.addEntry().withId("loginLogoutDiv").andText("Next")
                .andOptions("tipLocation:left;tipAnimation:fade");

        entry.addText("<p>&nbsp;</p>");
        entry.addText("<h2>Not logged in, eh?</h2>");
        entry.addText("<p>You are currently not logged in, this is why you can access this introductionary tour.</p>");
        entry.addText("<p>Unfortunately, this is exactly the reason why you cannot see much...</p>");

        entry = helpTour.addEntry().andText("Next");

        entry.addText("<h2>Some more info</h2>");
        entry.addText("<p>Before trying to log in, you can read along this tour to figure out what skysail notes is about.</p>");
        entry.addText("<p>But beware, this is the <i>backend</i>. It's a bit more techical... maybe you just want to see the <a href='/notesproduct/angular.html'>frontend</a>?</p>");
        entry.addText("<p>Otherwise, proceed by clicking 'Next':</p>");

        entry = helpTour.addEntry().withId("altRepresentations").andText("Next")
                .andOptions("tipLocation:right;tipAnimation:fade");

        entry.addText("<h2>Still with me?</h2>");
        entry.addText("<p>Great, you <b>are</b> curious ;)</p>");
        entry.addText("<p>One thing you should know about the backend is, that it is meant to be used by both humans <i>and</i> machines.</p>");
        entry.addText("<p>You as a human will retrieve the information as an html page. But there are other representations, like Json, which is meant for computers.</p>");

        entry = helpTour.addEntry().withId("loginLogoutDiv").andButton("Close")
                .andOptions("tipLocation:right;tipAnimation:fade");

        entry.addText("<p>&nbsp;</p>");
        entry.addText("<h2>Please enter...</h2>");
        entry.addText("<p>A last word about skysail, before I will tell you the default password...</p>");
        entry.addText("<p>The skysail <i>backend</i> you are about to enter represents a generic way to access all the functionality... it is meant for admins.</p>");
        entry.addText("<p>So, if this installations has not been tampered with yet, you can try to login with username 'admin' and password 'skysail'.</p>");

        return helpTour;
    }

    @Reference(multiple = true, optional = true, dynamic = true)
    public void addMenuProvider(MenuItemProvider provider) {
        if (provider == null) { // || provider.getMenuEntries() == null) {
            return;
        }
        menuProviders.add(provider);
    }

    public void removeMenuProvider(MenuItemProvider provider) {
        menuProviders.remove(provider);
    }

    @Reference(dynamic = true, optional = true, multiple = false)
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public void unsetUserManager(UserManager userManager) {
        this.userManager = null;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public Set<MenuItem> getMenuItems() {
        return menuProviders.stream()//
                .map(mp -> mp.getMenuEntries())//
                .filter(l -> (l != null))//
                .flatMap(mil -> mil.stream())//
                .collect(Collectors.toSet());
    }

    public String getRedirectTo() {
        if (properties == null) {
            return null;
        }
        if (!SecurityUtils.getSubject().isAuthenticated()) {
            return (String) properties.get("landingPage.notAuthenticated");
        }
        String landingPage = (String) properties.get("landingPage.authenticated");
        if (landingPage == null || landingPage.equals("") || landingPage.equals("/")) {
            return null;
        }
        return landingPage;
    }

    @Override
    public void updated(Dictionary<String, ?> properties) throws ConfigurationException {
        this.properties = properties;
    }

    public void clearCache(String username) {
        getAuthenticationService().clearCache(username);
    }

}
