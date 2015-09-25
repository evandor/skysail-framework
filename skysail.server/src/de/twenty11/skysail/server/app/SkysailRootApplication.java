package de.twenty11.skysail.server.app;

import io.skysail.server.app.SkysailApplication;
import io.skysail.server.menus.*;
import io.skysail.server.menus.MenuItem.Category;
import io.skysail.server.utils.MenuItemUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.apache.shiro.SecurityUtils;
import org.osgi.service.cm.*;
import org.osgi.service.component.ComponentContext;
import org.restlet.Request;

import aQute.bnd.annotation.component.*;
import de.twenty11.skysail.server.app.profile.*;
import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import de.twenty11.skysail.server.resources.*;
import de.twenty11.skysail.server.services.ResourceBundleProvider;

@Component(immediate = true, properties = { "service.pid=landingpages" })
public class SkysailRootApplication extends SkysailApplication implements ApplicationProvider, ResourceBundleProvider,
        ManagedService {

    private static final String CONFIG_IDENTIFIER_LANDINGPAGE_NOT_AUTHENTICATED = "landingPage.notAuthenticated";
    private static final String CONFIG_IDENTIFIER_LANDINGPAGE_AUTHENTICATED = "landingPage.authenticated";
    //private static final String CONFIG_IDENTIFIER_LOGIN_PAGE = "loginPage";

    private static final String ROOT_APPLICATION_NAME = "root";

    public static final String LOGIN_PATH = "/_login";
    public static final String DEMO_LOGIN_PATH = "/_demologin";
    public static final String PEERS_LOGIN_PATH = "/_remotelogin";

    public static final String LOGOUT_PATH = "/_logout";
    private static final String PROFILE_PATH = "/_profile";
    private static final String VERSION_PATH = "/_version";
    private static final String NAME_PATH = "/_name";
    private static final String LARGETESTS_PATH = "/_largetests";
    private static final String WEBCONSOLE_PATH = "/webconsole";
    private static final String WELCOME_PATH = "/welcome";

    private volatile Set<SkysailApplication> applications = new TreeSet<>();

    private Set<AtomicReference<MenuItemProvider>> menuProviders = new HashSet<>();

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

    @aQute.bnd.annotation.component.Reference(optional = true, dynamic = true, multiple = false)
    public void setApplicationListProvider(ServiceListProvider service) {
        super.setServiceListProvider(service);
    }

    public void unsetApplicationListProvider(ServiceListProvider service) {
        super.unsetServiceListProvider(service);
    }

    @Override
    protected void attach() {
//        super.attach();
        router.attach(new RouteBuilder("/", DefaultResource.class).noAuthenticationNeeded());

        // see ShiroDelegationAuthenticator
        router.attach(new RouteBuilder(LOGIN_PATH, LoginResource.class).noAuthenticationNeeded());
        router.attach(new RouteBuilder(DEMO_LOGIN_PATH, DemoLoginResource.class).noAuthenticationNeeded());
//        router.attach(new RouteBuilder(VERSION_PATH, VersionResource.class));
//        router.attach(new RouteBuilder(NAME_PATH, NameResource.class));
        router.attach(new RouteBuilder(PROFILE_PATH, ProfileResource.class));
        router.attach(new RouteBuilder(PROFILE_PATH + "/password/", PutPasswordResource.class));
//        router.attach(new RouteBuilder(LARGETESTS_PATH, LargeTestsResource.class));
//        router.attach(new RouteBuilder(LARGETESTS_PATH + "/{id}", LargeTestsFileResource.class));
//        router.attach(new RouteBuilder(WELCOME_PATH, WelcomeResource.class).noAuthenticationNeeded()); // need for tests... why?

        router.attach(new RouteBuilder(PEERS_LOGIN_PATH, RemoteLoginResource.class).noAuthenticationNeeded());

    }

    public Set<SkysailApplication> getApplications() {
        return this.applications;
    }


    @Reference(multiple = true, optional = true, dynamic = true)
    public void addMenuProvider(MenuItemProvider provider) {
        AtomicReference<MenuItemProvider> providerRef = new AtomicReference<MenuItemProvider>(provider);
        menuProviders.add(providerRef);
    }

    public void removeMenuProvider(MenuItemProvider provider) {
        menuProviders.remove(provider);
    }

    public Set<MenuItem> getMenuItems() {
        return menuProviders.stream()//
                .map(mp -> mp.get().getMenuEntries())//
                .filter(l -> (l != null))//
                .flatMap(mil -> mil.stream())//
                .collect(Collectors.toSet());
    }

    public String getRedirectTo() {
        if (properties == null) {
            return null;
        }
        if (!SecurityUtils.getSubject().isAuthenticated()) {
            return (String) properties.get(CONFIG_IDENTIFIER_LANDINGPAGE_NOT_AUTHENTICATED);
        }
        String landingPage = (String) properties.get(CONFIG_IDENTIFIER_LANDINGPAGE_AUTHENTICATED);
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

    public Set<MenuItem> getMainMenuItems(Request request) {
        Set<MenuItemProvider> providers = menuProviders.stream().map(m -> m.get()).collect(Collectors.toSet());
        return MenuItemUtils.getMenuItems(providers, request, Category.APPLICATION_MAIN_MENU);
    }

}
