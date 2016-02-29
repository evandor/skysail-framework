package io.skysail.server.restlet.resources;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.DateTimeConverter;
import org.apache.shiro.SecurityUtils;
import org.restlet.Application;
import org.restlet.data.Form;
import org.restlet.data.Reference;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.Options;
import org.restlet.resource.ServerResource;
import org.restlet.security.Role;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.twenty11.skysail.server.core.restlet.MessagesUtils;
import io.skysail.api.links.Link;
import io.skysail.api.links.LinkRelation;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.api.text.Translation;
import io.skysail.domain.core.ApplicationModel;
import io.skysail.domain.core.EntityModel;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.forms.FormField;
import io.skysail.server.forms.Tab;
import io.skysail.server.menus.MenuItem;
import io.skysail.server.services.PerformanceTimer;
import io.skysail.server.utils.LinkUtils;
import io.skysail.server.utils.ReflectionUtils;
import io.skysail.server.utils.ResourceUtils;
import io.skysail.server.utils.SkysailBeanUtils;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * Abstract base class for all skysail resources, parameterized with T, the type
 * of the entity handled.
 *
 * <p>
 * The entity can be something concrete (e.g. a contact) or a list of something
 * (e.g. a list of contacts).
 * </p>
 */
@Slf4j
@ToString(exclude = { "links" })
public abstract class SkysailServerResource<T> extends ServerResource {

    public static final String SKYSAIL_SERVER_RESTLET_FORM = "de.twenty11.skysail.server.core.restlet.form";
    public static final String SKYSAIL_SERVER_RESTLET_ENTITY = "de.twenty11.skysail.server.core.restlet.entity";
    public static final String SKYSAIL_SERVER_RESTLET_VARIANT = "de.twenty11.skysail.server.core.restlet.variant";

    public static final String FILTER_PARAM_NAME = "_filter";
    public static final String PAGE_PARAM_NAME = "_page";
    public static final String SEARCH_PARAM_NAME = "_search";
    

    public static final String NO_REDIRECTS = "noRedirects";
    public static final String INSPECT_PARAM_NAME = "_inspect";
    
    @Getter
    private Set<String> defaultMediaTypes = new HashSet<>();

    @Setter
    @Getter
    private Object currentEntity;

    private List<Link> links;

    // TODO use restlet context?
    private Map<ResourceContextId, String> stringContextMap = new HashMap<>();

    @Getter
    private ResourceContext resourceContext;

    public SkysailServerResource() {
        DateTimeConverter dateConverter = new DateConverter(null);
        dateConverter.setPattern("yyyy-MM-dd");
        dateConverter.setUseLocaleFormat(true);
        ConvertUtils.deregister(Date.class);
        ConvertUtils.register(dateConverter, Date.class);
        
        defaultMediaTypes.add("xml");
        defaultMediaTypes.add("json");
        defaultMediaTypes.add("x-yaml");
        defaultMediaTypes.add("csv");
        defaultMediaTypes.add("mailto");
    }

    /**
     * when overriding this method, don't forget to call <pre>super.doInit();</pre>.
     */
    @Override
    protected void doInit() {
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
            return "List of " + entityType.getSimpleName();
        }
        return entityType.getSimpleName();
    }

    @Options("json")
    public final SkysailResponse<ResourceContextResource> doOptions(Representation entity, Variant variant) { // NO_UCD (unused code)
        Set<PerformanceTimer> perfTimer = getApplication().startPerformanceMonitoring(
                this.getClass().getSimpleName() + ":doOptions");
        log.info("Request entry point: {}  @Options('json') with variant {}", this.getClass().getSimpleName(),
                variant);
        ResourceContextResource context = new ResourceContextResource(this);
        getApplication().stopPerformanceMonitoring(perfTimer);
        return new SkysailResponse<ResourceContextResource>(getResponse(), context);
    }

    /**
     * delegates to restlets getAttribute, but will decode the attribute as
     * well.
     */
    @Override
    public String getAttribute(String name) {
        String attribute = super.getAttribute(name);
        if (attribute != null) {
            return Reference.decode(attribute);
        }
        return null;
    }

    public Map<String, Translation> getMessages() {
        Map<String, Translation> msgs = new TreeMap<>();
        String key = getClass().getName() + ".message";
        Translation translated = ((SkysailApplication) getApplication()).translate(key, key, this);
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
            String fieldName = MessagesUtils.getSimpleName(f);
            addTranslation(msgs, application, f, baseKey, fieldName);
            addTranslation(msgs, application, f, baseKey + ".desc", null);
            addTranslation(msgs, application, f, baseKey + ".placeholder", null);

            String resourceBaseKey = this.getClass().getName() + "." + fieldName;
            addTranslation(msgs, application, f, resourceBaseKey, fieldName);
            addTranslation(msgs, application, f, resourceBaseKey + ".desc", null);
            addTranslation(msgs, application, f, resourceBaseKey + ".placeholder", null);

        });

        return msgs;
    }

    private void addTranslation(Map<String, Translation> msgs, Application application, FormField f, String key,
            String defaultMsg) {
        Translation translation = ((SkysailApplication) application).translate(key, defaultMsg, this);
        if (translation != null && translation.getValue() != null) {
            msgs.put(key, translation);
        } else if (defaultMsg != null) {
            msgs.put(key, new Translation(defaultMsg, null, Collections.emptySet()));
        }
    }

    public Class<?> getParameterizedType() {
        return ReflectionUtils.getParameterizedType(getClass());
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

    @SuppressWarnings("unchecked")
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
        Link linkheader = LinkUtils.fromResource(getApplication(), cls);
        if (linkheader == null) {
            return null;
        }
        getPathSubstitutions().accept(linkheader);
        return linkheader.getUri();
    }

    public void addToContext(ResourceContextId id, String value) {
        stringContextMap.put(id, value);
    }

    public void removeFromContext(ResourceContextId id) {
        stringContextMap.remove(id);
    }

    public String getFromContext(ResourceContextId id) {
        return stringContextMap.get(id);
    }

    protected void setUrlSubsitution(String identifierName, String id, String substitution) {
        Map<String, String> substitutions = new HashMap<>();
        substitutions.put("/"+identifierName+"/" + id, substitution);
        getContext().getAttributes().put(ResourceContextId.PATH_SUBSTITUTION.name(), substitutions);
    }

    protected T populate(T bean, Form form) {
        Map<String, Object> valuesMap = new HashMap<>();
        form.getNames().stream().forEach(key -> valuesMap.put(key, null));
        form.copyTo(valuesMap);
        try {
            SkysailBeanUtils beanUtilsBean = new SkysailBeanUtils(bean, ResourceUtils.determineLocale(this));
            beanUtilsBean.populate(bean, valuesMap);
            return bean;
        } catch (Exception e) {
            log.error("Error populating bean {} from form {}", bean, valuesMap, e);
            return null;
        }
    }

    public void copyProperties(T dest, T orig) {
        try {
            SkysailBeanUtils beanUtilsBean = new SkysailBeanUtils(orig, ResourceUtils.determineLocale(this));
            beanUtilsBean.copyProperties(dest, orig, this);
        } catch (Exception e) {
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

    public <S> S getService(Class<S> cls) {
        return cls.cast(getContext().getAttributes().get(cls.getName()));
    }

    public List<TreeRepresentation> getTreeRepresentation() {
        return Collections.emptyList();
    }
    
    public List<Tab> getTabs() {
        return Collections.emptyList();
    }
    
    public List<Tab> getTabs(Tab... tabs) {
        List<Tab> result = new ArrayList<>();
        Arrays.stream(tabs).forEach(result::add);
        return result;
    }
    
    public List<String> getEntityFields() {
        Class<?> type = getParameterizedType();
        ApplicationModel model = getApplication().getApplicationModel();
        Optional<EntityModel> entity = model.getEntityValues().stream().filter(e -> test(e, type)).findFirst();
        if (entity.isPresent()) {
            return new ArrayList<>(entity.get().getFieldNames());
        }
        return Collections.emptyList();
    }

    private boolean test(EntityModel e, Class<?> type) {
        return e.getId().equals(type.getName());
    }
}
