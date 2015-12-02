package io.skysail.server.app;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import org.apache.commons.lang.Validate;
import org.osgi.framework.*;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.*;
import org.osgi.service.event.EventAdmin;
import org.owasp.html.HtmlPolicyBuilder;
import org.restlet.*;
import org.restlet.data.*;
import org.restlet.data.Reference;
import org.restlet.ext.raml.*;
import org.restlet.resource.ServerResource;
import org.restlet.routing.Filter;
import org.restlet.security.*;
import org.restlet.util.RouteList;

import com.google.common.base.Predicate;

import de.twenty11.skysail.server.app.*;
import de.twenty11.skysail.server.core.restlet.*;
import de.twenty11.skysail.server.security.*;
import de.twenty11.skysail.server.services.*;
import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.*;
import io.skysail.api.repos.Repository;
import io.skysail.api.text.Translation;
import io.skysail.api.um.*;
import io.skysail.api.validation.ValidatorService;
import io.skysail.server.domain.core.Repositories;
import io.skysail.server.domain.jvm.ClassEntityModel;
import io.skysail.server.menus.MenuItem;
import io.skysail.server.restlet.filter.*;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.services.*;
import io.skysail.server.text.TranslationStoreHolder;
import io.skysail.server.utils.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * A skysail application is the entry point to provide additional functionality
 * to the skysail server.
 *
 * <p>Typically you will create a subclass of SkysailApplication like this:</p>
 *
 * <pre>
 * <code>
 *
 * {@literal @}org.osgi.service.component.annotations.Component(immediate = true)
 * public class MyApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {
 *
 *     private static final String APP_NAME = "myapp";
 *
 *     public DbClientApplication() {
 *          super(APP_NAME, new ApiVersion(1), Arrays.asList(Clip.class);
 *     }
 *
 *      {@literal @}Reference(dynamic = true, multiple = false, optional = false)
 *      public void setRepositories(Repositories repos) {
 *         super.setRepositories(repos);
 *      }
 *
 *      public void unsetRepositories(DbRepository repo) {
 *          super.setRepositories(null);
 *      }
 *
 * }
 * </code>
 * </pre>
 *
 * Important:
 *
 * A {@link SkysailApplication} is a {@link ApplicationModel}, which
 *
 * - handles access to its resources issues via its {@link AuthenticationService} and {@link AuthorizationService}
 *
 * - knows about the OSGi {@link ComponentContext}
 *
 * - deals with XSS issues via its {@link HtmlPolicyBuilder} - can encrypt/decrypt resource entitiies via its {@link EncryptorService}
 *
 * Concurrency note from parent class: instances of this class or its subclasses
 * can be invoked by several threads at the same time and therefore must be
 * thread-safe. You should be especially careful when storing state in member
 * variables.
 *
 */
@Slf4j
public abstract class SkysailApplication extends RamlApplication implements ApplicationProvider, ResourceBundleProvider,
        Comparable<ApplicationProvider> {

    private Map<ApplicationContextId, String> stringContextMap = new HashMap<>();

    /**
     * do not forget to add those media types as extensions in.
     *
     * {@link #createInboundRoot()}
     */
    public static final MediaType SKYSAIL_SERVER_SENT_EVENTS = MediaType.register("text/event-stream","Server Side Events");
    public static final MediaType SKYSAIL_TREE_FORM = MediaType.register("treeform", "Html Form as tree representation");
    public static final MediaType SKYSAIL_MAILTO_MEDIATYPE = MediaType.register("mailto", "href mailto target");
    public static final MediaType SKYSAIL_TIMELINE_MEDIATYPE = MediaType.register("timeline", "vis.js timeline representation");

    protected static volatile ServiceListProvider serviceListProvider;

    /** the restlet router. */
    protected volatile SkysailRouter router;

    @Getter
    private volatile ComponentContext componentContext;

    private volatile BundleContext bundleContext;
    private volatile HtmlPolicyBuilder noHtmlPolicyBuilder = new HtmlPolicyBuilder();
    private String home;
    private volatile List<String> parametersToHandle = new CopyOnWriteArrayList<String>();
    private volatile Map<String, String> parameterMap = new ConcurrentHashMap<String, String>();
    private volatile List<String> securedByAllRoles = new CopyOnWriteArrayList<String>();

    private List<MenuItem> applicationMenu;

    @Getter
    private ApiVersion apiVersion = new ApiVersion(1);

    private Map<String, Object> documentedEntities = new ConcurrentHashMap<>();

    /**
     * The core domain: a model defining an application with its entities, repositories,
     * entities fields, relations and so on. SkysailApplication itself cannot extend this
     * class as it has to be derived from a restlet application.
     */
    @Getter
    private  io.skysail.server.domain.core.ApplicationModel applicationModel;

    @Getter
    private  EncryptorService encryptorService;

    public SkysailApplication() {
        getEncoderService().getIgnoredMediaTypes().add(SkysailApplication.SKYSAIL_SERVER_SENT_EVENTS);
        getEncoderService().setEnabled(true);
    }

    public SkysailApplication(String appName) {
        this(appName, new ApiVersion(1));
    }

    public SkysailApplication(String appName, ApiVersion apiVersion) {
        this(appName, apiVersion, Collections.emptyList());
    }

    public SkysailApplication(String appName, ApiVersion apiVersion, List<Class<? extends Identifiable>> entityClasses) {
        this();
        log.debug("Instanciating new Skysail ApplicationModel '{}'", this.getClass().getSimpleName());
        this.home = appName;
        setName(appName);
        this.apiVersion = apiVersion;
        applicationModel = new io.skysail.server.domain.core.ApplicationModel(appName);
        entityClasses.forEach(cls -> applicationModel.add(EntityFactory.createFrom(cls)));
    }

    /**
     * probably you want to do something like
     * "router.attach(new RouteBuilder("", RootResource.class))".
     *
     * Important: Call <pre>super.attach()</pre> in the first line of this method.
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
        if (applicationModel.getEntityNames().isEmpty()) {
            log.warn("there are no entities defined for the applicationModel {}", applicationModel);
            return;
        }
        ClassEntityModel firstClassEntity = (ClassEntityModel) applicationModel.getEntity(applicationModel.getEntityNames().iterator().next());
        router.attach(new RouteBuilder("" , firstClassEntity.getListResourceClass()));
        router.attach(new RouteBuilder("/" , firstClassEntity.getListResourceClass()));

        applicationModel.getEntityNames().stream()
            .map(key -> applicationModel.getEntity(key))
            .map(ClassEntityModel.class::cast)
            .forEach(entity -> {
                router.attach(new RouteBuilder("/" + entity.getId(), entity.getListResourceClass()));
                router.attach(new RouteBuilder("/" + entity.getId() + "/", entity.getPostResourceClass()));
                router.attach(new RouteBuilder("/" + entity.getId() + "/{id}", entity.getEntityResourceClass()));
                router.attach(new RouteBuilder("/" + entity.getId() + "/{id}/", entity.getPutResourceClass()));
            });
    }

    /**
     * Remark: it seems I can use @Activate and @Deactive here (in this parent
     * class), but not @Reference!
     * http://stackoverflow.com/questions/12364484/providing
     * -di-methods-in-abstract-classes
     */
    @Activate
    protected void activate(ComponentContext componentContext) throws ConfigurationException {
        log.debug("Activating ApplicationModel {}", this.getClass().getName());
        this.componentContext = componentContext;
    }

    @Deactivate
    protected void deactivate(ComponentContext componentContext) {
        log.debug("Deactivating ApplicationModel {}", this.getClass().getName());
        this.componentContext = null;
        this.bundleContext = null;
        if (router != null) {
            router.detachAll();
        }
        log.debug("deactivating UserManagementApplication #" + this.hashCode());
        try {
            getApplication().stop();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        setInboundRoot((Restlet) null);
        setOutboundRoot((Restlet) null);
    }

    public Repository getRepository() {
        log.warn("calling default implementation of getRepository, which should be overwritten if the application provides a repository.");
        return null;
    }

    public Repository getRepository(Class<? extends Identifiable> entityClass) {
        Repository repository = applicationModel.getRepository(entityClass.getName());
        if (repository != null) {
            return repository;
        }
        log.warn("trying to access repository for entity class {}, but failed...", entityClass.getName());
        applicationModel.getRepositoryIdentifiers().stream().sorted().forEach(identifier -> {
            log.info(" - available: {}", identifier);
        });
        return getRepository();
    }

    protected void documentEntities(Object... entitiesToDocument) {
        Arrays.stream(entitiesToDocument).forEach(e -> {
            documentedEntities .put(e.getClass().getName(), e);
        });
    }
    
    public abstract EventAdmin getEventAdmin();

    public void setServiceListProvider(ServiceListProvider service) {
        serviceListProvider = service;
    }

    protected void unsetServiceListProvider(ServiceListProvider service) {
        serviceListProvider = null;
    }

    public Translation translate(String key, String defaultMsg, SkysailServerResource<?> resource) {

        Set<TranslationStoreHolder> translationStores = serviceListProvider.getTranslationStores();
        Optional<Translation> bestTranslationFromAStore = TranslationUtils.getBestTranslation(translationStores, key,
                resource);
        if (!bestTranslationFromAStore.isPresent()) {
            return new Translation(defaultMsg, null, Collections.emptySet());
        }
        Set<TranslationRenderServiceHolder> translationRenderServices = serviceListProvider.getTranslationRenderServices();
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
    public synchronized Restlet createInboundRoot() {

        log.info("creating new Router in {}", this.getClass().getName());
        router = new SkysailRouter(this);
        router.setApiVersion(apiVersion);
        // router.setDefaultMatchingQuery(true);

        log.info("adding extensions to metadata service");
        getMetadataService().addExtension("eventstream", SKYSAIL_SERVER_SENT_EVENTS);
        getMetadataService().addExtension("treeform", SKYSAIL_TREE_FORM);
        getMetadataService().addExtension("mailto", SKYSAIL_MAILTO_MEDIATYPE);
        getMetadataService().addExtension("timeline", SKYSAIL_TIMELINE_MEDIATYPE);

        // see
        // http://nexnet.wordpress.com/2010/09/29/clap-protocol-in-restlet-and-osgi/
        log.info("adding protocols");
        getConnectorService().getClientProtocols().add(Protocol.HTTP);
        // getConnectorService().getClientProtocols().add(Protocol.HTTPS);
        getConnectorService().getClientProtocols().add(Protocol.FILE);
        getConnectorService().getClientProtocols().add(Protocol.CLAP);
        // getConnectorService().getClientProtocols().add(Protocol.RIAP);

        // here or somewhere else? ServiceList?
        // enrolerService.setAuthorizationService(authorizationService);
        getContext().setDefaultEnroler((Enroler) serviceListProvider.getAuthorizationService());

        final class MyVerifier extends SecretVerifier {

            @Override
            public int verify(String identifier, char[] secret) {
                return 0;
            }

        }

        getContext().setDefaultVerifier(new MyVerifier());

        log.debug("attaching application-specific routes");

        attach();

        log.debug("creating tracer...");
        TracerFilter tracer = new TracerFilter(getContext());

        log.debug("creating original request filter...");
        OriginalRequestFilter originalRequestFilter = new OriginalRequestFilter(getContext());
        // blocker.setNext(originalRequestFilter);
        originalRequestFilter.setNext(router);

        log.debug("determining authentication service...");
        AuthenticationService authenticationService = getAuthenticationService();
        Authenticator authenticationGuard = null;
        if (authenticationService != null) {
            log.debug("setting authenticationGuard from authentication service");
            authenticationGuard = authenticationService.getAuthenticator(getContext());
        } else {
            log.warn("creating dummy authentication guard");
            authenticationGuard = new Authenticator(getContext()) {
                @Override
                protected boolean authenticate(Request request, Response response) {
                    return true;
                }
            };
        }

        Filter authorizationGuard = null;
        if (getAuthorizationService() != null && securedByAllRoles.size() > 0) {
            log.debug("setting authorization guard: new SkysailRolesAuthorizer");
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


//        RoleAuthorizer roleAuthorizer = new RoleAuthorizer();
//        roleAuthorizer.setAuthorizedRoles(Scopes.toRoles("status"));
//        roleAuthorizer.setNext(router);
//
//        ChallengeAuthenticator bearerAuthenticator = new ChallengeAuthenticator(getContext(),
//                ChallengeScheme.HTTP_OAUTH_BEARER, "ApplicationModel");
//
//        bearerAuthenticator.setVerifier(new TokenVerifier(new org.restlet.data.Reference("yourTokenAuthURI")));
//        bearerAuthenticator.setNext(roleAuthorizer);


        return tracer;
    }

    @Override
    public RamlSpecificationRestlet getRamlSpecificationRestlet(Context context) {
         RamlSpecificationRestlet ramlRestlet = super.getRamlSpecificationRestlet(context);
         ramlRestlet.setBasePath("http://localhost:2017/usermanagement/{version}");
         ramlRestlet.setApiVersion("v33");
         return ramlRestlet;
    }

    public void attachToRouter(String key, Class<? extends ServerResource> executor) {
        router.attach(key, executor);
    }

    public void attachToRouter(String key, Restlet restlet) {
        router.attach(key, restlet);
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

    public List<RouteBuilder> getRouteBuildersForResource(Class<? extends ServerResource> cls) {
        return router.getRouteBuildersForResource(cls);
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
                log.info("found resource bundle for language '{}', classloader {}:", language,
                        classLoader.toString());
                Enumeration<String> keys = resourceBundleEn.getKeys();
                while (keys.hasMoreElements()) {
                    String nextElement = keys.nextElement();
                    log.info(" {} -> {}", nextElement, resourceBundleEn.getString(nextElement));
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
        return serviceListProvider.getAuthenticationService();
    }

    public AuthorizationService getAuthorizationService() {
        return serviceListProvider.getAuthorizationService();
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

    @Override
    public int compareTo(ApplicationProvider o) {
        return this.getApplication().getName().compareTo(o.getApplication().getName());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName()).append(" (SkysailApplication)\n");
        sb.append("Home: ").append(home).append(", \nRouter: ").append(router).append("\n");
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

    public Set<PerformanceMonitor> getPerformanceMonitors() {
        Set<PerformanceMonitor> performanceMonitors = serviceListProvider.getPerformanceMonitors();
        return Collections.unmodifiableSet(performanceMonitors);
    }

    protected void addToAppContext(ApplicationContextId id, String value) {
        stringContextMap.put(id, value);
    }

    public String getFromContext(ApplicationContextId id) {
        return stringContextMap.get(id);
    }

    public ValidatorService getValidatorService() {
        return serviceListProvider.getValidatorService();
    }


    public Set<PerformanceTimer> startPerformanceMonitoring(String identifier) {
        Collection<PerformanceMonitor> performanceMonitors = serviceListProvider.getPerformanceMonitors();
        return performanceMonitors.stream().map(monitor -> {
            return monitor.start(identifier);
        }).collect(Collectors.toSet());
    }

    public void stopPerformanceMonitoring(Set<PerformanceTimer> perfTimer) {
        perfTimer.stream().forEach(timer -> timer.stop());
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

    public void setRepositories(Repositories repos) {
        applicationModel.setRepositories(repos);
    }

    public List<MenuItem> getMenuEntries() {
        MenuItem appMenu = new MenuItem(getName(), "/" + getName() + getApiVersion().getVersionPath());
        appMenu.setCategory(MenuItem.Category.APPLICATION_MAIN_MENU);
        return Arrays.asList(appMenu);
    }

    protected void addService(Object service) {
        getContext().getAttributes().put(service.getClass().getName(), service);
    }


}
