package io.skysail.server.app;

import io.skysail.api.documentation.DocumentationProvider;
import io.skysail.api.favorites.FavoritesService;
import io.skysail.api.forms.*;
import io.skysail.api.peers.PeersProvider;
import io.skysail.api.text.Translation;
import io.skysail.api.um.*;
import io.skysail.api.validation.ValidatorService;
import io.skysail.server.restlet.filter.*;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.services.*;
import io.skysail.server.text.TranslationStoreHolder;
import io.skysail.server.utils.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.Validate;
import org.osgi.framework.*;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.event.EventAdmin;
import org.owasp.html.HtmlPolicyBuilder;
import org.restlet.*;
import org.restlet.data.*;
import org.restlet.data.Reference;
import org.restlet.resource.*;
import org.restlet.routing.Filter;
import org.restlet.security.*;
import org.restlet.util.RouteList;
import org.slf4j.*;

import aQute.bnd.annotation.component.*;

import com.google.common.base.Predicate;

import de.twenty11.skysail.server.app.*;
import de.twenty11.skysail.server.core.restlet.*;
import de.twenty11.skysail.server.help.HelpTour;
import de.twenty11.skysail.server.metrics.MetricsService;
import de.twenty11.skysail.server.security.*;
import de.twenty11.skysail.server.services.*;

/**
 * A skysail application is the entry point to provide additional functionality
 * to the skysail server.
 * 
 * Typically you will create a subclass of SkysailApplication like this:
 * 
 * <pre>
 * <code>
 * 
 * {@literal @}aQute.bnd.annotation.component.Component(immediate = true)
 * public class MyApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {
 * 
 *     private static final String APP_NAME = "myapp";
 * 
 *     public DbClientApplication() {
 *          super(APP_NAME);
 *     }
 *     
 *     {@literal @}Override
 *     protected void attach() {
 *        router.attach(new RouteBuilder("/clips", ClipsResource.class));
 *        router.attach(new RouteBuilder("/clips/", PostClipResource.class));
 *        router.attach(new RouteBuilder("/clips/{id}", ClipResource.class));
 *        router.attach(new RouteBuilder("/clips/{id}/", PutClipResource.class)); 
 *        ...
 *     }
 *     
 *     public List&lt;MenuItem&gt; getMenuEntries() {
 *         MenuItem appMenu = new MenuItem(APP_NAME, "/" + APP_NAME);
 *         appMenu.setCategory(MenuItem.Category.APPLICATION_MAIN_MENU);
 *         return Arrays.asList(appMenu);
 *     }
 * }
 * </code>
 * </pre>
 * 
 * A {@link SkysailApplication} is a {@link Application}, which
 * 
 * - has a number of {@link Resource}s attached to it - handles access to its
 * resources issues via its {@link AuthenticationService} and
 * {@link AuthorizationService} - knows about the OSGi {@link ComponentContext}
 * - deals with XSS issues via its {@link HtmlPolicyBuilder} - can
 * encrypt/decrypt resource entitiies via its {@link EncryptorService}
 * 
 * Concurrency note from parent class: instances of this class or its subclasses
 * can be invoked by several threads at the same time and therefore must be
 * thread-safe. You should be especially careful when storing state in member
 * variables.
 * 
 */
@Slf4j
public abstract class SkysailApplication extends Application implements ApplicationProvider, ResourceBundleProvider,
        Comparable<ApplicationProvider> {

    /** slf4j based logger implementation. */
    private static Logger logger = LoggerFactory.getLogger(SkysailApplication.class);

    private Map<ApplicationContextId, String> stringContextMap = new HashMap<>();

    public static final String APPLICATION_API_PATH = "/api";
    public static final String APPLICATION_ENTITIES_PATH = "/entities";
    public static final String APPLICATION_LINKS_PATH = "/links";

    /**
     * do not forget to add those media types as extensions in.
     * 
     * {@link #createInboundRoot()}
     */
    public static final MediaType SKYSAIL_HTMLFORM_MEDIATYPE = MediaType.register("htmlform", "HTML Form document");
    public static final MediaType SKYSAIL_GRAPH = MediaType.register("graph", "graph representation");
    public static final MediaType SKYSAIL_SERVER_SENT_EVENTS = MediaType.register("text/event-stream",
            "Server Side Events");
    public static final MediaType SKYSAIL_TREE_FORM = MediaType
            .register("treeform", "Html Form as tree representation");
    public static final MediaType SKYSAIL_SHTML_MEDIATYPE = MediaType.register("shtml", "Server Side Include");
    public static final MediaType SKYSAIL_MAILTO_MEDIATYPE = MediaType.register("mailto", "href mailto target");

    protected static AtomicReference<ServiceListProvider> serviceListProviderRef = new AtomicReference<>();

    /** the restlet router. */
    protected volatile SkysailRouter router;

    private volatile ComponentContext componentContext;
    private volatile BundleContext bundleContext;
    private volatile AuthenticationService authenticationService;
    private volatile HtmlPolicyBuilder noHtmlPolicyBuilder = new HtmlPolicyBuilder();
    private volatile AuthorizationService authorizationService;
    private volatile EventAdmin eventAdmin;
    private String home;
    private volatile List<String> parametersToHandle = new CopyOnWriteArrayList<String>();
    private volatile Map<String, String> parameterMap = new ConcurrentHashMap<String, String>();
    private volatile List<String> securedByAllRoles = new CopyOnWriteArrayList<String>();
    private volatile EncryptorService encryptorService;
    private volatile FavoritesService favoritesService;
    private volatile MetricsService metricsService;
    private volatile Set<HookFilter> filters = Collections.synchronizedSet(new HashSet<>());
    private volatile Set<PerformanceMonitor> performanceMonitors = Collections.synchronizedSet(new HashSet<>());
    //private volatile ValidatorService validatorService;

    private List<MenuItem> applicationMenu;

    public SkysailApplication() {
        getEncoderService().setEnabled(true);
    }

    /**
     * probably you want to do something like
     * "router.attach(new RouteBuilder("", RootResource.class))".
     *
     * <p>
     * You can add authorization information like this for all routes:
     * </p>
     * 
     * <p>
     * <code>router.setAuthorizationDefaults(anyOf("usermanagement.user", "admin"));</code>
     * </p>
     *
     * <p>
     * or for specific routes:
     * </p>
     *
     * <p>
     * <code>router.attach(new RouteBuilder("/",
     * UsersResource.class).authorizeWith(anyOf("admin")));</code>
     * </p>
     */
    protected void attach() {
        DocumentationProvider documentationProvider = serviceListProviderRef.get().getDocumentationProvider().get();
        if (documentationProvider == null) {
            log.warn("not documentation provider available. No Selfdocumentation of APIs.");
            return;
        }
        Map<String, Class<? extends ServerResource>> docuMap = documentationProvider.getResourceMap();
        docuMap.keySet().stream().forEach(key -> {
            router.attach(new RouteBuilder(key, docuMap.get(key)));
        });
    }

    /**
     * setting name.
     * 
     * @param home
     */
    public SkysailApplication(String home) {
        this();
        logger.debug("Instanciating new Skysail Application '{}'", this.getClass().getSimpleName());
        this.home = home;
        setName(home);
    }

    /**
     * Remark: it seems I can use @Activate and @Deactive here (in this parent
     * class), but not @Reference!
     * http://stackoverflow.com/questions/12364484/providing
     * -di-methods-in-abstract-classes
     */
    @Activate
    protected void activate(ComponentContext componentContext) throws ConfigurationException {
        logger.debug("Activating Application {}", this.getClass().getName());
        this.componentContext = componentContext;
    }

    @Deactivate
    protected void deactivate(ComponentContext componentContext) {
        logger.debug("Deactivating Application {}", this.getClass().getName());
        this.componentContext = null;
        this.authenticationService = null;
        this.authorizationService = null;
        this.bundleContext = null;
        this.eventAdmin = null;
        this.favoritesService = null;
        this.filters.clear();
        this.metricsService = null;

//        if (serviceListProvider != null) {
//            SkysailComponent skysailComponent = serviceListProvider.getSkysailComponent();
//            skysailComponent.getDefaultHost().detach(this.getClass());
//        }
        if (router != null) {
            router.detachAll();
        }
        logger.debug("deactivating UserManagementApplication #" + this.hashCode());
        try {
            getApplication().stop();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        setInboundRoot((Restlet) null);
        setOutboundRoot((Restlet) null);
    }
    
    protected void setServiceListProvider(ServiceListProvider service) {
        SkysailApplication.serviceListProviderRef.set(service);
    }

    protected void unsetServiceListProvider(ServiceListProvider service) {
        SkysailApplication.serviceListProviderRef.compareAndSet(service, null);
    }

    // @Override
    public String translate(String key, String defaultMsg, Resource resource, Object... substitutions) {

        Set<TranslationStoreHolder> translationStores = serviceListProviderRef.get().getTranslationStores();
        Optional<Translation> bestTranslationFromAStore = TranslationUtils.getBestTranslation(translationStores, key,
                resource);
        if (!bestTranslationFromAStore.isPresent()) {
            return defaultMsg;
        }
        Set<TranslationRenderServiceHolder> translationRenderServices = serviceListProviderRef.get().getTranslationRenderServices();
        return TranslationUtils.render(translationRenderServices, bestTranslationFromAStore.get());
    }

    /**
     * @return the bundle context.
     */
    public BundleContext getBundleContext() {
        if (this.bundleContext != null) {
            return bundleContext;
        }
        return componentContext != null ? componentContext.getBundleContext() : null;
    }

    protected void setHome(String home) {
        this.home = home;
    }

    // TODO getName vs getHome?
    public String getHome() {
        return home;
    }

    @Override
    public Restlet createInboundRoot() {

        logger.info("creating new Router in {}", this.getClass().getName());
        router = new SkysailRouter(getContext());
        // router.setDefaultMatchingQuery(true);

        logger.info("adding extensions to metadata service");
        getMetadataService().addExtension("htmlform", SKYSAIL_HTMLFORM_MEDIATYPE);
        getMetadataService().addExtension("shtml", SKYSAIL_SHTML_MEDIATYPE);
        getMetadataService().addExtension("eventstream", SKYSAIL_SERVER_SENT_EVENTS);
        getMetadataService().addExtension("treeform", SKYSAIL_TREE_FORM);
        getMetadataService().addExtension("graph", SKYSAIL_GRAPH);
        getMetadataService().addExtension("mailto", SKYSAIL_MAILTO_MEDIATYPE);

        // see
        // http://nexnet.wordpress.com/2010/09/29/clap-protocol-in-restlet-and-osgi/
        logger.info("adding protocols");
        getConnectorService().getClientProtocols().add(Protocol.HTTP);
        // getConnectorService().getClientProtocols().add(Protocol.HTTPS);
        getConnectorService().getClientProtocols().add(Protocol.FILE);
        getConnectorService().getClientProtocols().add(Protocol.CLAP);
        // getConnectorService().getClientProtocols().add(Protocol.RIAP);

        // here or somewhere else? ServiceList?
        // enrolerService.setAuthorizationService(authorizationService);
        getContext().setDefaultEnroler((Enroler) authorizationService);

        final class MyVerifier extends SecretVerifier {

            @Override
            public int verify(String identifier, char[] secret) {
                return 0;
            }

        }

        getContext().setDefaultVerifier(new MyVerifier());

        logger.debug("attaching application-specific routes");

        attach();

        logger.debug("creating tracer...");
        TracerFilter tracer = new TracerFilter(getContext(), getEventAdmin());

        logger.debug("creating original request filter...");
        OriginalRequestFilter originalRequestFilter = new OriginalRequestFilter(getContext());
        // blocker.setNext(originalRequestFilter);
        originalRequestFilter.setNext(router);

        logger.debug("determining authentication service...");
        AuthenticationService authenticationService = getAuthenticationService();
        Authenticator authenticationGuard = null;
        if (authenticationService != null) {
            logger.debug("setting authenticationGuard from authentication service");
            authenticationGuard = authenticationService.getAuthenticator(getContext());
        } else {
            logger.warn("creating dummy authentication guard");
            authenticationGuard = new Authenticator(getContext()) {
                @Override
                protected boolean authenticate(Request request, Response response) {
                    return true;
                }
            };
        }

        Filter authorizationGuard = null;
        if (getAuthorizationService() != null && securedByAllRoles.size() > 0) {
            logger.debug("setting authorization guard: new SkysailRolesAuthorizer");
            authorizationGuard = new SkysailRolesAuthorizer(securedByAllRoles);
        }
        // tracer -> linker -> authenticationGuard (-> authorizationGuard) ->
        // originalRequest -> router
        if (authorizationGuard != null) {
            authorizationGuard.setNext(originalRequestFilter);
            authenticationGuard.setNext(authorizationGuard);
        } else {
            authenticationGuard.setNext(originalRequestFilter);
        }
        tracer.setNext(authenticationGuard);
        return tracer;
    }

    public void attachToRouter(String key, Class<? extends ServerResource> executor) {
        router.attach(key, executor);
    }

    public void detachFromRouter(Class<?> executor) {
        router.detach(executor);
    }

    public RouteList getRoutes() {
        return router.getRoutes();
    }

    public Map<String, RouteBuilder> getRoutesMap() {
        return router.getRoutesMap();
    }

    public Map<String, RouteBuilder> getSkysailRoutes() {
        return router.getRouteBuilders();
    }

    @Override
    public SkysailApplication getApplication() {
        return this;
    }

    @Override
    public <T extends SkysailServerResource<?>> List<String> getTemplatePaths(Class<T> cls) {
        List<String> paths = router.getTemplatePathForResource(cls);
        List<String> result = new ArrayList<>();
        for (String path : paths) {
            result.add("/" + getName() + path);
        }
        return result;
    }

    /**
     * get the route builders.
     * 
     * @param cls
     * @return list of route builders
     */
    public <T extends SkysailServerResource<?>> List<RouteBuilder> getRouteBuilders(Class<T> cls) {
        if (router == null) {
            return Collections.emptyList();
        }
        return router.getRouteBuildersForResource(cls);
    }

    @Override
    public List<ResourceBundle> getResourceBundles() {
        List<ResourceBundle> result = new ArrayList<>();
        addResourceBundleIfExistent(result, "en", this.getClass().getClassLoader());
        addResourceBundleIfExistent(result, "de", this.getClass().getClassLoader());
        return result;
    }

    private void addResourceBundleIfExistent(List<ResourceBundle> result, String language, ClassLoader classLoader) {
        try {
            ResourceBundle resourceBundleEn = ResourceBundle.getBundle("translations/messages", new Locale(language),
                    classLoader);
            if (resourceBundleEn != null) {
                logger.info("found resource bundle for language '{}', classloader {}:", language,
                        classLoader.toString());
                Enumeration<String> keys = resourceBundleEn.getKeys();
                while (keys.hasMoreElements()) {
                    String nextElement = keys.nextElement();
                    logger.info(" {} -> {}", nextElement, resourceBundleEn.getString(nextElement));
                }
                result.add(resourceBundleEn);
            }
        } catch (MissingResourceException mre) {
            // ok
        }
    }

    public String getLinkTo(Reference reference, Class<? extends ServerResource> cls) {
        List<String> relativePaths = router.getTemplatePathForResource(cls);
        return reference.toString() + relativePaths.get(0);
    }

    public void setEventAdmin(EventAdmin eventAdmin) {
        this.eventAdmin = eventAdmin;
    }

    public void unsetEventAdmin() {
        this.eventAdmin = null;
    }

    public void setComponentContext(ComponentContext componentContext) {
        this.componentContext = componentContext;
    }

    /**
     * Some bundles set the componentContext, others (via blueprint) only the
     * bundleContext... need to revisit
     * 
     * @return
     */
    public Bundle getBundle() {
        if (this.bundleContext != null) {
            return this.bundleContext.getBundle();
        }
        if (componentContext == null) {
            return null;
        }
        return componentContext.getBundleContext().getBundle();
    }

    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    public void setFavoritesService(FavoritesService service) {
        logServiceWasSet("Favorites", service);
        this.favoritesService = service;
    }

    public FavoritesService getFavoritesService() {
        return favoritesService;
    }

//    public void setPeersProvider(PeersProvider service) {
//        logServiceWasSet("Peers", service);
//        this.peersProvider = service;
//    }
//
//    public PeersProvider getPeersProvider() {
//        return peersProvider;
//    }

    public EventAdmin getEventAdmin() {
        return eventAdmin;
    }

    public void setAuthenticationService(AuthenticationService authService) {
        this.authenticationService = authService;
    }

    public AuthorizationService getAuthorizationService() {
        return authorizationService;
    }

    /**
     * setter
     * 
     * @param service
     */
    public void setAuthorizationService(AuthorizationService service) {
        logServiceWasSet("Authorization", service);
        this.authorizationService = service;
    }

    public MetricsService getMetricsService() {
        return metricsService;
    }

    public void setMetricsService(MetricsService service) {
        logServiceWasSet("Metrics", service);
        this.metricsService = service;
    }

    public void handleParameters(List<String> parametersToHandle) {
        this.parametersToHandle = parametersToHandle;
    }

    public List<String> getParametersToHandle() {
        return parametersToHandle;
    }

    public void addRequestParameter(String paramName, String value) {
        parameterMap.put(paramName, value);
    }

    /**
     * returns policy.
     * 
     * @param entityClass
     * @param fieldName
     * @return
     */
    public HtmlPolicyBuilder getHtmlPolicy(Class<?> entityClass, String fieldName) {
        HtmlPolicyBuilder result = noHtmlPolicyBuilder;
        List<java.lang.reflect.Field> fields = ReflectionUtils.getInheritedFields(entityClass);
        for (java.lang.reflect.Field field : fields) {
            if (!field.getName().equals(fieldName)) {
                continue;
            }
            Field formField = field.getAnnotation(Field.class);
            if (formField == null) {
                continue;
            }
            HtmlPolicy htmlPolicy = formField.htmlPolicy();
            List<String> allowedElements = htmlPolicy.getAllowedElements();
            HtmlPolicyBuilder htmlPolicyBuilder = new HtmlPolicyBuilder();
            htmlPolicyBuilder.allowElements(allowedElements.toArray(new String[allowedElements.size()]));
            return htmlPolicyBuilder;
        }

        return result;
    }

    /**
     * get the encryption parameter.
     * 
     * @param entityClass
     * @param fieldName
     * @return
     */
    public String getEncryptionParameter(Class<?> entityClass, String fieldName) {
        List<java.lang.reflect.Field> fields = ReflectionUtils.getInheritedFields(entityClass);
        for (java.lang.reflect.Field field : fields) {
            if (!field.getName().equals(fieldName)) {
                continue;
            }
            Field formField = field.getAnnotation(Field.class);
            if (formField == null) {
                continue;
            }
            return formField.encryptWith();
        }

        return null;
    }

    protected void setSecuredByRoles(String... rolenames) {
        Validate.noNullElements(rolenames);
        this.securedByAllRoles = Arrays.asList(rolenames);
    }

    public List<String> getSecuredByAllRoles() {
        return Collections.unmodifiableList(securedByAllRoles);
    }

    public HelpTour getHelpTour() {
        return null;
    }

    public void setEncryptorService(EncryptorService encryptorService) {
        this.encryptorService = encryptorService;
    }

    public EncryptorService getEncryptorService() {
        return encryptorService;
    }

    public void unsetEncryptorService() {
        this.encryptorService = null;
    }

    @Override
    public int compareTo(ApplicationProvider o) {
        return this.getApplication().getName().compareTo(o.getApplication().getName());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName()).append(" (SkysailApplication)\n");
        sb.append("Home: ").append(home).append(", \nRouter: ").append(router).append("\n");
        sb.append("AuthenticationService: ").append(authenticationService).append("\n");
        // sb.append("AuthorizationService: ").append(authorizationService).append("\n");
        return sb.toString();
    }

    protected Role getApplicationRole(String applicationRoleName) {
        String roleIdentifier = getName() + "." + applicationRoleName;
        Role applicationRole = new Role(roleIdentifier);
        getRoles().add(applicationRole);
        return applicationRole;
    }

    protected Role getFrameworkRole(String name) {
        Role existingRole = getRole(name);
        if (existingRole != null) {
            return existingRole;
        }
        Role role = new Role(name);
        getRoles().add(role);
        return role;
    }

    /**
     * xxx.
     * 
     * @param roles
     * @return
     */
    public static Predicate<String[]> anyOf(String... roles) {
        List<RolePredicate> predicates = Arrays.stream(roles).map(r -> new RolePredicate(r))
                .collect(Collectors.toList());
        return com.google.common.base.Predicates.or(predicates);
    }

    /**
     * yyy.
     * 
     * @param roles
     * @return
     */
    public static Predicate<String[]> allOf(String... roles) {
        List<RolePredicate> predicates = Arrays.stream(roles).map(r -> new RolePredicate(r))
                .collect(Collectors.toList());
        return com.google.common.base.Predicates.and(predicates);
    }

    private void logServiceWasSet(String name, Object service) {
        if (service == null) {
            logger.trace("{} service was set to null for application '{}'", name, getName());
        } else {
            logger.trace("{} service was set to '{}' for application '{}'", new Object[] { name,
                    service.getClass().getSimpleName(), getName() });
        }
    }

    public synchronized <R extends SkysailServerResource<T>, T> void addFilter(HookFilter<R, T> filter) {
        logger.debug("adding hookfilters to application '{}'", getName());
        filters.add(filter);
    }

    public <R extends SkysailServerResource<T>, T> void removeFilter(HookFilter<R, T> filter) {
        logger.debug("removing filter from application '{}'", getName());
        filters.remove(filter);
    }

    /**
     * sets filters.
     * 
     * @param hookFilters
     */
    public void setFilters(Set<HookFilter> hookFilters) {
        logger.debug("setting hookfilters to application '{}'", getName());
        filters.clear();
        filters.addAll(hookFilters);
    }

    public Set<HookFilter> getFilters() {
        return filters;
    }

    public synchronized void addMonitor(PerformanceMonitor monitor) {
        logger.debug("adding performanceMonitor to application '{}'", getName());
        performanceMonitors.add(monitor);
    }

    public <R extends SkysailServerResource<T>, T> void removeMonitor(PerformanceMonitor monitor) {
        logger.debug("removing performanceMonitor from application '{}'", getName());
        performanceMonitors.remove(monitor);
    }

    public Set<PerformanceMonitor> getPerformanceMonitors() {
        return Collections.unmodifiableSet(performanceMonitors);
    }

    protected void addToAppContext(ApplicationContextId id, String value) {
        stringContextMap.put(id, value);
    }

    public String getFromContext(ApplicationContextId id) {
        return stringContextMap.get(id);
    }
    
    public AtomicReference<ValidatorService> getValidatorService() {
        return serviceListProviderRef.get().getValidatorService();
    }


    public Set<PerformanceTimer> startPerformanceMonitoring(String identifier) {
        return this.performanceMonitors.stream().map(monitor -> {
            return monitor.start(identifier);
        }).collect(Collectors.toSet());
    }

    public void stopPerformanceMonitoring(Set<PerformanceTimer> perfTimer) {
        perfTimer.stream().forEach(timer -> timer.stop());
    }

    public String getRemotePath(String installation, String subpath) {
        PeersProvider peersProvider = serviceListProviderRef.get().getPeersProvider().get();
        return peersProvider.getPath(installation) + subpath;
    }

    public List<MenuItem> getMenuEntriesWithCache() {
        if (applicationMenu == null) {
            applicationMenu = createMenuEntries();
        }
        return applicationMenu;
    }

    public List<MenuItem> createMenuEntries() {
        return Collections.emptyList();
    }
    
    public void invalidateMenuCache() {
        applicationMenu = null;
    }

}
