package io.skysail.server.restlet.resources;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.links.*;
import io.skysail.api.utils.StringParserUtils;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.RequestHandler;
import io.skysail.server.restlet.filter.AbstractResourceFilter;
import io.skysail.server.utils.*;

import java.lang.reflect.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.beanutils.*;
import org.apache.commons.beanutils.converters.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.restlet.Application;
import org.restlet.data.*;
import org.restlet.resource.*;
import org.restlet.security.Role;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.twenty11.skysail.server.core.FormField;
import de.twenty11.skysail.server.core.restlet.*;
import de.twenty11.skysail.server.services.MenuItem;

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
@ToString(exclude = { "beanUtilsBean","linkheader" })
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
    private T currentEntity;

    private List<Link> linkheader;

    @Setter
    @Getter
    private String desc;

    private Map<ResourceContextId, String> stringContextMap = new HashMap<>();

    private Map<ResourceContextId, Map<String, String>> mapContextMap = new HashMap<>();

    @Getter
    private ResourceContext resourceContext;

    private BeanUtilsBean beanUtilsBean = new BeanUtilsBean(new ConvertUtilsBean() {
        @SuppressWarnings("unchecked")
        @Override
        public Object convert(String value, @SuppressWarnings("rawtypes") Class clazz) {
            if (clazz.isEnum()) {
                return Enum.valueOf(clazz, value);
            } else if (clazz.equals(Date.class)) {
                if (StringUtils.isEmpty(value)) {
                    return null;
                }
                SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
                try {
                    return sdf.parse(value);
                } catch (Exception e) {
                    log.info("could not parse date '{}' with pattern {}", value, DATE_PATTERN);
                }
                return null;
            } else {
                return super.convert(value, clazz);
            }
        }
    });

    public SkysailServerResource() {
        DateTimeConverter dateConverter = new DateConverter(null);

        dateConverter.setPattern("yyyy-MM-dd");
        dateConverter.setUseLocaleFormat(true);
        ConvertUtils.deregister(Date.class);
        ConvertUtils.register(dateConverter, Date.class);
        beanUtilsBean.getConvertUtils().register(dateConverter, Date.class);

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

    /**
     * get Messages.
     *
     * @return map with messages
     */
    public Map<String, String> getMessages() {
        Application application = getApplication();
        Map<String, String> msgs = new HashMap<>();
        msgs.put("content.header",
                "default msg from de.twenty11.skysail.server.core.restlet.SkysailServerResource.getMessages()");
        String key = getClass().getName() + ".message";
        String translated = ((SkysailApplication) application).translate(key, key, this, true);
        msgs.put("content.header", translated);

        //HeadersUtils.getHeaders(getResponse()).add("X-Resource-Description", translated);

        return msgs;
    }

    /**
     * get Messages.
     *
     * @param fields
     *            a list of fields
     * @return messages the messages
     */
    public Map<String, String> getMessages(Map<String, FormField> fields) {
        Map<String, String> msgs = getMessages();
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

    private void addTranslation(Map<String, String> msgs, Application application, FormField f, String key,
            String defaultMsg) {
        String translation = ((SkysailApplication) application).translate(key, defaultMsg, this, false);
        if (translation != null) {
            msgs.put(key, translation);
        } else if (defaultMsg != null) {
            msgs.put(key, defaultMsg);
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
        if (linkheader != null) {
            return linkheader;
        }
        SkysailApplication app = getApplication();
        List<Link> links = Arrays.asList(classes).stream() //
                .map(cls -> LinkUtils.fromResource(app, cls))//
                .filter(lh -> {
                    return lh != null;// && lh.isApplicable();
                }).collect(Collectors.toList());

        links.addAll(getAssociatedLinks());

        links.forEach(getPathSubstitutions());
        this.linkheader = links;
        return links;
    }

    /**
     * if the current resource is a {@link ListServerResource}, the associated
     * EntityServerResource (if existent) is analyzed for its own links.
     *
     * <p>
     * For each entity of the listServerResource, and for each associated link
     * (which serves as a template), a new link is created and is having its
     * path placeholders substituted. So, if the current ListServerResource has
     * a list with two entities of a type which defines three classes in its
     * getLinks method, we'll get six links in the result.
     * </p>
     */
    private List<? extends Link> getAssociatedLinks() {
        if (!(this instanceof ListServerResource)) {
            return Collections.emptyList();
        }
        ListServerResource<?> listServerResource = (ListServerResource<?>) this;
        List<Class<? extends SkysailServerResource<?>>> entityResourceClasses = listServerResource
                .getAssociatedServerResources();
        // List<Class<? extends EntityServerResource<?>>> entityResourceClass =
        // listServerResource.getAssociatedEntityResources();
        T entity = getCurrentEntity();
        List<Link> result = new ArrayList<>();

        if (entityResourceClasses != null && entity instanceof List) {
            List<SkysailServerResource<?>> esrs = ResourceUtils.createSkysailServerResources(entityResourceClasses,
                    this);

            for (SkysailServerResource<?> esr : esrs) {
                List<Link> entityLinkTemplates = esr.getAuthorizedLinks();
                for (Object object : (List<?>) entity) {
                    String id = guessId(object);
                    entityLinkTemplates.stream().filter(lh -> {
                        return lh.getRole().equals(LinkRole.DEFAULT);
                    }).forEach(link -> addLink(link, esr, id, listServerResource, result));
                }
            }
        }
        return result;

    }

    private String guessId(Object object) {
        if (object instanceof Identifiable) {
            Identifiable identifiable = (Identifiable) object;
            return identifiable.getId().replace("#", "");
        }
        if (object instanceof Map) {
            Map<String, Object> map = (Map) object;
            if (map.get("@rid") != null) {
                return map.get("@rid").toString().replace("#", "");
            }
            if (map.get("id") != null) {
                return map.get("id").toString().replace("#", "");
            }
        }
        Map<String, Object> map = OrientDbUtils.toMap(object);
        if (map != null) {
            if (map.get("@rid") != null) {
                return map.get("@rid").toString().replace("#", "");
            }
        }

        return "NO_ID";
    }

    private void addLink(Link linkTemplate, Resource entityResource, String id, ListServerResource<?> resource,
            List<Link> result) {
        String path = linkTemplate.getUri();
        // does this ever work?
        String href = StringParserUtils.substitutePlaceholders(path, entityResource);

        // hmmm... last resort
        if (id != null && href.contains("{") && href.contains("}")) {
            href = href.replaceFirst(StringParserUtils.placeholderPattern.toString(), id);
        }

        Link newLink = new Link.Builder(linkTemplate)// .uri()
                .uri(href).role(LinkRole.LIST_VIEW).relation(LinkRelation.ITEM).refId(id).build();
        // substituePlaceholders(entityResource, id).build()
        result.add(newLink);
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
        if (linkheader != null) {
            return linkheader;
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
            beanUtilsBean.populate(bean, valuesMap);
            return bean;
        } catch (Exception e) {
            log.error("Error populating bean {} from form {}", bean, valuesMap, e);
            return null;
        }
    }

    protected void copyProperties(T dest, T orig) {
        try {
            beanUtilsBean.copyProperties(dest, orig);
        } catch (Exception e) {
            log.error("Error copying from bean {} to bean {}", orig, dest);
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



}
