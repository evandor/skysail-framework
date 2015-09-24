package io.skysail.server.restlet.resources;

import io.skysail.api.links.*;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.api.text.Translation;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.forms.FormField;
import io.skysail.server.menus.MenuItem;
import io.skysail.server.restlet.RequestHandler;
import io.skysail.server.restlet.filter.AbstractResourceFilter;
import io.skysail.server.services.PerformanceTimer;
import io.skysail.server.utils.*;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.beanutils.*;
import org.apache.commons.beanutils.converters.*;
import org.apache.shiro.SecurityUtils;
import org.restlet.Application;
import org.restlet.data.*;
import org.restlet.representation.*;
import org.restlet.resource.*;
import org.restlet.security.Role;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.twenty11.skysail.server.core.restlet.*;

/**
 * Abstract base class for all skysail resources, parameterized with T, the type
 * of the entity handled.
 *
 * <p>
 * The entity can be something concrete (e.g. a contact) or a list of something
 * (e.g. a list of contacts).
 * </p>
 *
 * <p>
 * Those methods are called from the framework, see {@link RequestHandler} and
 * the various {@link AbstractResourceFilter} implementations. The
 * RequestHandler provides a chain of filters (depending on the current request)
 * which will make sure that aspects like error management, logging, security
 * issues and the like are always dealt with in the same way.
 * </p>
 */
@Slf4j
@ToString(exclude = { "links" })
public abstract class SkysailServerResource<T> extends ServerResource {

    private static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String ATTRIBUTES_INTERNAL_REQUEST_ID = "de.twenty11.skysail.server.restlet.SkysailServerResource.requestId";
    public static final String SKYSAIL_SERVER_RESTLET_FORM = "de.twenty11.skysail.server.core.restlet.form";
    public static final String SKYSAIL_SERVER_RESTLET_ENTITY = "de.twenty11.skysail.server.core.restlet.entity";
    public static final String SKYSAIL_SERVER_RESTLET_VARIANT = "de.twenty11.skysail.server.core.restlet.variant";

    public static final String FILTER_PARAM_NAME = "_filter";
    public static final String PAGE_PARAM_NAME = "_page";

    public static final String NO_REDIRECTS = "noRedirects";
    public static final String INSPECT_PARAM_NAME = "_inspect";

    @Setter
    @Getter
    private Object currentEntity;

    private List<Link> links;

    private Map<ResourceContextId, String> stringContextMap = new HashMap<>();

    private Map<ResourceContextId, Map<String, String>> mapContextMap = new HashMap<>();

    @Getter
    private ResourceContext resourceContext;

    @Getter
    private Set<String> restrictedToMediaTypes = new HashSet<>();

    public SkysailServerResource() {
        DateTimeConverter dateConverter = new DateConverter(null);

        dateConverter.setPattern("yyyy-MM-dd");
        dateConverter.setUseLocaleFormat(true);
        ConvertUtils.deregister(Date.class);
        ConvertUtils.register(dateConverter, Date.class);
//        beanUtilsBean.getConvertUtils().register(dateConverter, Date.class);
    }

    /**
     * when overriding this method, don't forget to call <pre>super.doInit();</pre>.
     */
    @Override
    protected void doInit() throws ResourceException {
        resourceContext = new ResourceContext(getApplication(), this);
    }

    @Override
    public SkysailApplication getApplication() {
        Application app = super.getApplication();
        return (app instanceof SkysailApplication) ? (SkysailApplication)app : null;
    }

    /**
     * Typically you will query some kind of repository here and return the
     * result (of type T, where T could be a List).
     * @param subject
     *
     * @return entity of Type T (can be a list as well)
     */
    public abstract T getEntity();

    public T getEntity(String installation) {
        return getEntity();
    }

    public abstract LinkRelation getLinkRelation();

    public String getEntityType() {
        Class<?> entityType = (Class<?>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
        if (this instanceof ListServerResource) {
            return "List of " + entityType.getName();
        }
        return entityType.getName();
    }

    /**
     * todo
     */
    @Options("json")
    public final SkysailResponse<ResourceContextResource> doOptions(Representation entity, Variant variant) {
        Set<PerformanceTimer> perfTimer = getApplication().startPerformanceMonitoring(
                this.getClass().getSimpleName() + ":doOptions");
        log.info("Request entry point: {}  @Options('json') with variant {}", this.getClass().getSimpleName(),
                variant);
        ResourceContextResource context = new ResourceContextResource(this);
        getApplication().stopPerformanceMonitoring(perfTimer);
        return new SkysailResponse<ResourceContextResource>(context);
    }

    /*
     * delegates to restlets getAttribute, but will decode the attribute as
     * well.
     */
    public String getAttribute(String name) {
        String attribute = super.getAttribute(name);
        if (attribute != null) {
            return Reference.decode(attribute);
        }
        return null;
    }

    public Map<String, Translation> getMessages() {
        Application application = getApplication();
        Map<String, Translation> msgs = new TreeMap<>();
//        msgs.put("content.header",
//                new Trans"default msg from de.twenty11.skysail.server.core.restlet.SkysailServerResource.getMessages()");
        String key = getClass().getName() + ".message";
        Translation translated = ((SkysailApplication) application).translate(key, key, this);
        msgs.put("content.header", translated);
        return msgs;
    }

    /**
     * get Messages.
     *
     * @param fields
     *            a list of fields
     * @return messages the messages
     */
    public Map<String, Translation> getMessages(Map<String, FormField> fields) {
        Map<String, Translation> msgs = getMessages();
        if (fields == null) {
            return msgs;
        }
        Application application = getApplication();
        fields.values().stream().forEach(f -> {

            Class<? extends Object> entityClass = null;
            if (getCurrentEntity() != null) {
                if (getCurrentEntity() instanceof List && ((List<?>)getCurrentEntity()).size() > 0) {
                    entityClass = ((List<?>)getCurrentEntity()).get(0).getClass();
                } else {
                    entityClass = getCurrentEntity().getClass();
                }
            }

            String baseKey = MessagesUtils.getBaseKey(entityClass, f);
            addTranslation(msgs, application, f, baseKey, MessagesUtils.getSimpleName(f));
            addTranslation(msgs, application, f, baseKey + ".desc", null);
            addTranslation(msgs, application, f, baseKey + ".placeholder", null);
        });

        return msgs;
    }

    private void addTranslation(Map<String, Translation> msgs, Application application, FormField f, String key,
            String defaultMsg) {
        Translation translation = ((SkysailApplication) application).translate(key, defaultMsg, this);
        if (translation != null) {
            msgs.put(key, translation);
        } else if (defaultMsg != null) {
            msgs.put(key, new Translation(defaultMsg, null, Collections.emptySet()));
        }
    }

    public Class<?> getParameterizedType() {
        ParameterizedType parameterizedType = getParameterizedType(getClass());
        Type firstActualTypeArgument = parameterizedType.getActualTypeArguments()[0];
        if (firstActualTypeArgument.getTypeName().startsWith("java.util.Map")) {
            return Map.class;
        }
        return (Class<?>) firstActualTypeArgument;
    }

    private ParameterizedType getParameterizedType(Class cls) {
        Type genericSuperclass = cls.getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            return (ParameterizedType) genericSuperclass;
        }
        return getParameterizedType(cls.getSuperclass());
    }

    /**
     * Reasoning: not overwriting those two (overloaded) methods gives me a
     * jackson deserialization issue. I need to define which method I want to be
     * ignored by jackson.
     *
     * @see org.restlet.resource.ServerResource#setLocationRef(org.restlet.data.Reference)
     */
    @JsonIgnore
    @Override
    public void setLocationRef(Reference locationRef) {
        super.setLocationRef(locationRef);
    }

    @Override
    public void setLocationRef(String locationUri) {
        super.setLocationRef(locationUri);
    }

    /**
     * creates a list of links for the provided {@link SkysailServerResource}
     * classes.
     *
     * <p>
     * This method is executed only once for the current resource, and the
     * result is cached for further requests.
     * </p>
     *
     * <p>
     * If the resource has associated resources, those links are added as well.
     * </p>
     */
    @SafeVarargs
    public final List<Link> getLinks(Class<? extends SkysailServerResource<?>>... classes) {
        if (links == null) {
            links = LinkUtils.fromResources(this, getCurrentEntity(), classes);
        }
        return links;
    }

    public List<Link> getLinks(List<Class<? extends SkysailServerResource<?>>> links) {
        return getLinks(links.toArray(new Class[links.size()]));
    }

    /**
     * example: l -&gt; { l.substitute("spaceId", spaceId).substitute("id",
     * getData().getPage().getRid()); };
     *
     * @return consumer for pathSubs
     */
    public Consumer<? super Link> getPathSubstitutions() {
        return l -> {
            String uri = l.getUri();
            l.setUri(LinkUtils.replaceValues(uri, getRequestAttributes()));
        };
    }

    /**
     * A resource provides a list of links it references. This is the complete
     * list of links, including links the current user is not authorized to
     * follow.
     *
     * @see SkysailServerResource#getAuthorizedLinks()
     *
     *      for example
     *
     *      <pre>
     * <code>
     * return getLinkheader(PostMyEntityResource.class);
     * </code>
     * </pre>
     *
     * @return result
     */
    public List<Link> getLinks() {
        if (links != null) {
            return links;
        }
        return new ArrayList<Link>();
    }

    /**
     * Links might be removed from the framework if the current user isn't
     * authorized to call them.
     *
     * @return result
     */
    public List<Link> getAuthorizedLinks() {
        List<Link> allLinks = getLinks();
        return allLinks.stream().filter(link -> isAuthorized(link)).collect(Collectors.toList());
    }

    private boolean isAuthorized(@NonNull Link link) {
        boolean authenticated = SecurityUtils.getSubject().isAuthenticated();
        List<Role> clientRoles = getRequest().getClientInfo().getRoles();
        if (!link.getNeedsAuthentication()) {
            return true;
        }
        List<String> clienRoleNames = clientRoles.stream().map(cr -> cr.getName()).collect(Collectors.toList());
        if (link.getRolesPredicate() != null) {
            if (link.getRolesPredicate().apply(clienRoleNames.toArray(new String[clienRoleNames.size()]))) {
                return true;
            }
        } else {
            if (authenticated) {
                return true;
            }
        }
        return false;
    }

    public String redirectTo() {
        return null;
    }

    public String redirectTo(Class<? extends SkysailServerResource<?>> cls) {
        SkysailApplication app = getApplication();
        Link linkheader = LinkUtils.fromResource(app, cls);
        if (linkheader == null) {
            return null;
        }
        getPathSubstitutions().accept(linkheader);
        return linkheader.getUri();
    }

    public void addToContext(ResourceContextId id, String value) {
        stringContextMap.put(id, value);
    }

    public void addToContext(ResourceContextId id, Map<String, String> map) {
        mapContextMap.put(id, map);
    }

    public void removeFromContext(ResourceContextId id) {
        stringContextMap.remove(id);
    }

    public String getFromContext(ResourceContextId id) {
        return stringContextMap.get(id);
    }

    public Map<String, String> getMapFromContext(ResourceContextId id) {
        return mapContextMap.get(id);
    }

    protected T populate(T bean, Form form) {
        Map<String, String> valuesMap = form.getValuesMap();
        try {

            SkysailBeanUtils beanUtilsBean = new SkysailBeanUtils(ResourceUtils.determineLocale(this));
            //DateTimeConverter dateConverter = new DateConverter(null);
            //beanUtilsBean.getConvertUtils().register(dateConverter, Date.class);
            beanUtilsBean.populate(bean, valuesMap);
            return bean;
        } catch (Exception e) {
            log.error("Error populating bean {} from form {}", bean, valuesMap, e);
            return null;
        }
    }

    protected void copyProperties(T dest, T orig) {
        try {
            SkysailBeanUtils beanUtilsBean = new SkysailBeanUtils(ResourceUtils.determineLocale(this));
            //DateTimeConverter dateConverter = new DateConverter(null);
           // beanUtilsBean.getConvertUtils().register(dateConverter, Date.class);
            beanUtilsBean.copyProperties(dest, orig, this);
        } catch (Exception e) {
            //log.error("Error copying from bean {} to bean {}", orig, dest);
            throw new RuntimeException("Error copying beans", e);
        }
    }

    protected Map<String, String> describe(T bean) {
        try {
            return BeanUtils.describe(bean);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public List<String> getFields() {
        List<Field> inheritedFields = getInheritedFields(getParameterizedType());
        return inheritedFields.stream().map(f -> f.getName()).collect(Collectors.toList());
    }

    public List<MenuItem> getAppNavigation() {
        return Collections.emptyList();
    }

    private List<java.lang.reflect.Field> getInheritedFields(Class<?> type) {
        List<java.lang.reflect.Field> result = new ArrayList<java.lang.reflect.Field>();

        Class<?> i = type;
        while (i != null && i != Object.class) {
            while (i != null && i != Object.class) {
                for (java.lang.reflect.Field field : i.getDeclaredFields()) {
                    if (!field.isSynthetic()) {
                        result.add(field);
                    }
                }
                i = i.getSuperclass();
            }
        }

        return result;
    }

    public Set<String> getRestrictedToMediaTypes() {
        return Collections.emptySet();
    }

    public Set<String> getRestrictedToMediaTypes(String... supportedMediaTypes) {
        HashSet<String> result = new HashSet<String>();
        Arrays.stream(supportedMediaTypes).forEach(smt -> result.add(smt));
        return result;
    }

    public Set<String> getDefaultMediaTypes() {
        HashSet<String> result = new HashSet<>();
        result.add("xml");
        result.add("json");
        result.add("x-yaml");
        result.add("csv");
        return result;
    }

}
