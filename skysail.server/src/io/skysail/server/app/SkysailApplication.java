package io.skysail.server.app;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import org.apache.commons.lang.Validate;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.event.EventAdmin;
import org.owasp.html.HtmlPolicyBuilder;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;
import org.restlet.ext.raml.RamlApplication;
import org.restlet.ext.raml.RamlSpecificationRestlet;
import org.restlet.resource.ServerResource;
import org.restlet.routing.Filter;
import org.restlet.security.Authenticator;
import org.restlet.security.Enroler;
import org.restlet.security.Role;
import org.restlet.security.SecretVerifier;
import org.restlet.util.RouteList;

import com.google.common.base.Predicate;

import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import de.twenty11.skysail.server.core.restlet.SkysailRouter;
import io.skysail.api.text.Translation;
import io.skysail.api.um.AuthenticationService;
import io.skysail.api.um.AuthorizationService;
import io.skysail.api.validation.ValidatorService;
import io.skysail.domain.Identifiable;
import io.skysail.domain.core.Repositories;
import io.skysail.domain.core.repos.Repository;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.HtmlPolicy;
import io.skysail.server.ApplicationContextId;
import io.skysail.server.domain.jvm.ClassEntityModel;
import io.skysail.server.menus.MenuItem;
import io.skysail.server.restlet.filter.OriginalRequestFilter;
import io.skysail.server.restlet.filter.TracerFilter;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.security.RolePredicate;
import io.skysail.server.security.SkysailRolesAuthorizer;
import io.skysail.server.services.PerformanceMonitor;
import io.skysail.server.services.PerformanceTimer;
import io.skysail.server.services.ResourceBundleProvider;
import io.skysail.server.text.TranslationStoreHolder;
import io.skysail.server.utils.ReflectionUtils;
import io.skysail.server.utils.TranslationUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * A skysail application is the entry point to provide additional functionality
 * to the skysail server.
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
    
    @Getter
    @org.osgi.service.component.annotations.Reference
    private volatile List<PerformanceMonitor> performanceMonitors;
    
    @Getter
    @org.osgi.service.component.annotations.Reference(cardinality = ReferenceCardinality.MANDATORY)
    public volatile ValidatorService validatorService;

    private Map<ApplicationContextId, String> stringContextMap = new HashMap<>(); // NOSONAR

    public static final MediaType SKYSAIL_SERVER_SENT_EVENTS = MediaType.register("text/event-stream","Server Side Events");
    public static final MediaType SKYSAIL_TREE_FORM = MediaType.register("treeform", "Html Form as tree representation");
    public static final MediaType SKYSAIL_MAILTO_MEDIATYPE = MediaType.register("mailto", "href mailto target");
    public static final MediaType SKYSAIL_TIMELINE_MEDIATYPE = MediaType.register("timeline", "vis.js timeline representation");
    public static final MediaType SKYSAIL_STANDLONE_APP_MEDIATYPE = MediaType.register("standalone", "standalone application representation");

    protected static volatile ServiceListProvider serviceListProvider;

    /**
     * The core domain: a model defining an application with its entities, repositories,
     * entities fields, relations and so on. SkysailApplication itself cannot extend this
     * class as it has to be derived from a restlet application.
     */
    @Getter
    private io.skysail.domain.core.ApplicationModel applicationModel;

    /** the restlet router. */
    protected volatile SkysailRouter router;

    @Getter
    private volatile ComponentContext componentContext;

    @Getter
    private ApiVersion apiVersion = new ApiVersion(1);
    
    private volatile BundleContext bundleContext;
    private volatile HtmlPolicyBuilder noHtmlPolicyBuilder = new HtmlPolicyBuilder();
    private volatile List<String> parametersToHandle = new CopyOnWriteArrayList<>();
    private volatile Map<String, String> parameterMap = new ConcurrentHashMap<>();
    private volatile List<String> securedByAllRoles = new CopyOnWriteArrayList<>();

    private List<MenuItem> applicationMenu;
    private Map<String, Object> documentedEntities = new ConcurrentHashMap<>();

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
        setName(appName);
        this.apiVersion = apiVersion;
        applicationModel = new io.skysail.domain.core.ApplicationModel(appName);
        entityClasses.forEach(cls -> applicationModel.addOnce(EntityFactory.createFrom(cls)));
    }

    protected void attach() {
        if (applicationModel.getEntityIds().isEmpty()) {
            log.warn("there are no entities defined for the applicationModel {}", applicationModel);
            return;
        }
        ClassEntityModel firstClassEntity = (ClassEntityModel) applicationModel.getEntity(applicationModel.getEntityIds().iterator().next());
        router.attach(new RouteBuilder("" , firstClassEntity.getListResourceClass()));
        router.attach(new RouteBuilder("/" , firstClassEntity.getListResourceClass()));

        applicationModel.getEntityIds().stream()
            .map(key -> applicationModel.getEntity(key)) // NOSONAR
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
        log.info("Activating ApplicationModel {}", this.getClass().getName());
        this.componentContext = componentContext;
    }

    @Deactivate
    protected void deactivate(ComponentContext componentContext) { // NOSONAR
        log.info("Deactivating ApplicationModel {}", this.getClass().getName());
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
        applicationModel.getRepositoryIds().stream().sorted().forEach(identifier -> { // NOSONAR
            log.info(" - available: {}", identifier);
        });
        return getRepository();
    }

    protected void documentEntities(Object... entitiesToDocument) {
        Arrays.stream(entitiesToDocument).forEach(e -> { // NOSONAR
            documentedEntities .put(e.getClass().getName(), e);
        });
    }
    
    public abstract EventAdmin getEventAdmin();

    public static void setServiceListProvider(ServiceListProvider service) {
        serviceListProvider = service;
    }

    protected static void unsetServiceListProvider(ServiceListProvider service) { // NOSONAR
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

    @Override
    public synchronized Restlet createInboundRoot() {

        log.info("creating new Router in {}", this.getClass().getName());
        router = new SkysailRouter(this);
        router.setApiVersion(apiVersion);

        log.info("adding extensions to metadata service");
        getMetadataService().addExtension("eventstream", SKYSAIL_SERVER_SENT_EVENTS);
        getMetadataService().addExtension("treeform", SKYSAIL_TREE_FORM);
        getMetadataService().addExtension("mailto", SKYSAIL_MAILTO_MEDIATYPE);
        getMetadataService().addExtension("timeline", SKYSAIL_TIMELINE_MEDIATYPE);
        getMetadataService().addExtension("standalone", SKYSAIL_STANDLONE_APP_MEDIATYPE);
        //getMetadataService().addExtension("text/prs.skysail-uikit", SKYSAIL_UIKIT_MEDIATYPE);

        getMetadataService().addExtension("x-www-form-urlencoded", MediaType.APPLICATION_WWW_FORM);

        // see
        // http://nexnet.wordpress.com/2010/09/29/clap-protocol-in-restlet-and-osgi/
        log.info("adding protocols");
        getConnectorService().getClientProtocols().add(Protocol.HTTP);
        getConnectorService().getClientProtocols().add(Protocol.FILE);
        getConnectorService().getClientProtocols().add(Protocol.CLAP);

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
        originalRequestFilter.setNext(router);

        log.debug("determining authentication service...");
        AuthenticationService authenticationService = getAuthenticationService();
        Authenticator authenticationGuard;
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
        if (getAuthorizationService() != null && !securedByAllRoles.isEmpty()) {
            log.debug("setting authorization guard: new SkysailRolesAuthorizer");
            authorizationGuard = new SkysailRolesAuthorizer(securedByAllRoles);
        }
        if (authorizationGuard != null) {
            authorizationGuard.setNext(originalRequestFilter);
            authenticationGuard.setNext(authorizationGuard);
        } else {
            authenticationGuard.setNext(originalRequestFilter);
        }
        tracer.setNext(authenticationGuard);

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
        if (router == null) {
            log.error("router of application '{}' is null! - access the skysail server at least once with your browser", this.getName());
            return Collections.emptyMap();
        }
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
        } catch (MissingResourceException mre) { // NOSONAR
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
        sb.append("Router: ").append(router).append("\n");
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

    protected void addToAppContext(ApplicationContextId id, String value) {
        stringContextMap.put(id, value);
    }

    public String getFromContext(ApplicationContextId id) {
       return stringContextMap.get(id);
    }

//    public ValidatorService getValidatorService() {
//        return serviceListProvider.getValidatorService();
//    }

    public Set<PerformanceTimer> startPerformanceMonitoring(String identifier) {
        return performanceMonitors.stream().map(monitor -> monitor.start(identifier)).collect(Collectors.toSet());
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
        MenuItem appMenu = new MenuItem(getName(), "/" + getName() + getApiVersion().getVersionPath(), this);
        appMenu.setCategory(MenuItem.Category.APPLICATION_MAIN_MENU);
        return Arrays.asList(appMenu);
    }

    protected void addService(Object service) {
        getContext().getAttributes().put(service.getClass().getName(), service);
    }


}
