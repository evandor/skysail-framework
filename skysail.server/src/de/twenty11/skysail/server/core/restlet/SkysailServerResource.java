package de.twenty11.skysail.server.core.restlet;

import io.skysail.api.links.Link;
import io.skysail.api.links.LinkRelation;
import io.skysail.api.links.LinkRole;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.DateTimeConverter;
import org.apache.shiro.SecurityUtils;
import org.restlet.Application;
import org.restlet.data.Form;
import org.restlet.data.Reference;
import org.restlet.resource.Resource;
import org.restlet.resource.ServerResource;
import org.restlet.security.Role;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.twenty11.skysail.api.domain.Identifiable;
import de.twenty11.skysail.server.app.SkysailApplication;
import de.twenty11.skysail.server.core.FormField;
import de.twenty11.skysail.server.core.restlet.filter.AbstractResourceFilter;
import de.twenty11.skysail.server.core.restlet.utils.StringParserUtils;
import de.twenty11.skysail.server.utils.ResourceUtils;
import etm.core.configuration.EtmManager;
import etm.core.monitor.EtmMonitor;

/**
 * Abstract base class for all skysail resources, parameterized with T, the type
 * of the entity handled.
 * 
 * <p>
 * Subclasses should override the methods
 * SkysailServerResource#addEntity(Object),
 * SkysailServerResource#updateEntity(Object) and/or
 * SkysailServerResource#eraseEntity() if they support adding, updating and/or
 * deletion of the referenced resource. It is assumed that
 * SkysailServerResources will always support GET requests, which are dealt with
 * by the abstract method @link {@link SkysailServerResource#setEntity()}.
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
@ToString(exclude = { "beanUtilsBean" })
public abstract class SkysailServerResource<T> extends ServerResource {

    public static final String ATTRIBUTES_INTERNAL_REQUEST_ID = "de.twenty11.skysail.server.restlet.SkysailServerResource.requestId";

    public static final String SKYSAIL_SERVER_RESTLET_FORM = "de.twenty11.skysail.server.core.restlet.form";
    public static final String SKYSAIL_SERVER_RESTLET_ENTITY = "de.twenty11.skysail.server.core.restlet.entity";

    protected static final EtmMonitor etmMonitor = EtmManager.getEtmMonitor();

    /**
     * the payload, set once it was retrieved from #getEntity()
     */
    @Setter
    @Getter
    private T currentEntity;

    private String desc;

    private Map<ResourceContextId, String> stringContextMap = new HashMap<>();

    private Map<ResourceContextId, Map<String, String>> mapContextMap = new HashMap<>();

    BeanUtilsBean beanUtilsBean = new BeanUtilsBean(new ConvertUtilsBean() {
        @Override
        public Object convert(String value, Class clazz) {
            if (clazz.isEnum()) {
                return Enum.valueOf(clazz, value);
            } else if (clazz.equals(Date.class) && value.equals("")) {
                return null;
            } else {
                return super.convert(value, clazz);
            }
        }
    });

    private List<Link> linkheader;

    @Getter
    @Setter
    private String metaRefreshTarget;

    public SkysailServerResource() {
        DateTimeConverter dateConverter = new DateConverter(null);

        dateConverter.setPattern("yyyy-MM-dd");
        dateConverter.setUseLocaleFormat(true);
        ConvertUtils.deregister(Date.class);
        ConvertUtils.register(dateConverter, Date.class);
        beanUtilsBean.getConvertUtils().register(dateConverter, Date.class);
    }

    @Override
    public SkysailApplication getApplication() {
        return (SkysailApplication) super.getApplication();
    }

    /**
     * Typically you will query some kind of repository here and return the
     * result (of type T).
     * 
     * @return entity of Type T (can be a list as well)
     */
    public abstract T getEntity();

    // TODO rename
    /**
     * xxx.
     * 
     * @return entity type as string
     */
    public String getEntityType() {
        Class<?> entityType = (Class<?>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
        if (this instanceof ListServerResource) {
            return "List of " + entityType.getName();
        }
        return entityType.getName();
    }

    public String getAttribute(String name) {
        String attribute = super.getAttribute(name);
        if (attribute != null) {
            return Reference.decode(attribute);
        }
        return null;
    }
    
    /**
     * @return the type of relation this resource represents, e.g. LIST, ITEM,
     *         ...
     */
    public abstract LinkRelation getLinkRelation();

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
        return msgs;
    }

    /**
     * get Messages.
     * 
     * @param fields
     *            a list of fields
     * @return messages the messages
     */
    public Map<String, String> getMessages(List<FormField> fields) {
        Map<String, String> msgs = getMessages();
        if (fields == null) {
            return msgs;
        }
        Application application = getApplication();
        fields.stream().forEach(f -> {

            Class<? extends Object> entityClass = null;
            if (f.getEntity() != null) {
                entityClass = f.getEntity().getClass();
            } else {
                entityClass = f.getCls();
            }

            String baseKey = MessagesUtils.getBaseKey(entityClass, f);
            addTranslation(msgs, application, f, baseKey);
            addTranslation(msgs, application, f, baseKey + ".desc");
            addTranslation(msgs, application, f, baseKey + ".placeholder");
        });

        return msgs;
    }

    private void addTranslation(Map<String, String> msgs, Application application, FormField f, String key) {
        String defaultMsg = MessagesUtils.getSimpleName(f);
        String translation = ((SkysailApplication) application).translate(key, defaultMsg, this, false);
        if (translation != null) {
            msgs.put(key, translation);
        } else {
            msgs.put(key, key);
        }
    }

    public Class<?> getParameterType() {
        return (Class<?>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
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
     * get Link.
     * 
     * @param classes
     *            the classes
     * @return linkheader the linkheader
     */
    @SafeVarargs
    public final List<Link> getLinkheader(Class<? extends SkysailServerResource<?>>... classes) {
        if (linkheader != null) {
            return linkheader;
        }
        SkysailApplication app = getApplication();
        List<Link> linkheader = Arrays.asList(classes).stream() //
                .map(cls -> ServerLink.fromResource(app, cls))//
                .filter(lh -> {
                    return lh != null;
                }).collect(Collectors.toList());

        linkheader.addAll(getAssociatedLinks());

        linkheader.forEach(getPathSubstitutions());
        this.linkheader = linkheader;
        return linkheader;
    }

    private List<? extends Link> getAssociatedLinks() {
        if (!(this instanceof ListServerResource)) {
            return Collections.emptyList();
        }
        ListServerResource<?> listServerResource = (ListServerResource<?>) this;
        Class<? extends EntityServerResource<?>> entityResourceClass = listServerResource.getAssociatedEntityResource();
        T entity = getCurrentEntity();
        List<Link> result = new ArrayList<>();

        if (entityResourceClass != null && entity instanceof List) {
            EntityServerResource<?> esr = ResourceUtils.createEntityServerResource(entityResourceClass, this);
            List<Link> entityLinks = esr.getLinkheaderAuthorized();
            for (Object object : (List<?>) entity) {
                String id = guessId(object);
                entityLinks.stream().filter(lh -> {
                    return lh.getRole().equals(LinkRole.DEFAULT);
                }).forEach(link -> addLinkHeader(link, esr, id, listServerResource, result));
            }
        }
        return result;

    }

    private String guessId(Object object) {
        if (object instanceof Identifiable) {
            return ((Identifiable) object).getId().replace("#", "");
        }
        return "";
    }

    private void addLinkHeader(Link link, Resource entityResource, String id, ListServerResource<?> resource,
            List<Link> result) {
        String path = link.getUri();
        String href = StringParserUtils.substitutePlaceholders(path, entityResource);

        // hmmm... last resort
        if (id != null && href.contains("{") && href.contains("}")) {
            // path = path.replace("{id}", id);
            href = href.replaceFirst(StringParserUtils.placeholderPattern.toString(), id);
        }

        result.add(new Link.Builder(href).relation(LinkRelation.ITEM).title(link.getTitle())
                .role(LinkRole.LIST_VIEW).refId(id).build());

    }

    /**
     * example: l -&gt; { l.substitute("spaceId", spaceId).substitute("id",
     * getData().getPage().getRid()); };
     *
     * @return consumer for pathSubs
     */
    public Consumer<? super Link> getPathSubstitutions() {
        return l -> {
        };
    }

    /**
     * A resource provides a list of links it references. This is the complete
     * list of links, including links the current user is not authorized to
     * follow.
     * 
     * @see SkysailServerResource#getLinkheaderAuthorized()
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
    public List<Link> getLinkheader() {
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
    public List<Link> getLinkheaderAuthorized() {
        List<Link> allLinks = getLinkheader();
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

    public void setDescription(String desc) {
        this.desc = desc;
    }

    public String getDescription() {
        return desc;
    }

    public String redirectTo() {
        return null;
    }

    public String redirectTo(Class<? extends SkysailServerResource<?>> cls) {
        SkysailApplication app = getApplication();
        Link linkheader = ServerLink.fromResource(app, cls);
        if (linkheader == null) {
            return null;
        }
        return linkheader.getUri();
    }

    public void addToContext(ResourceContextId id, String value) {
        stringContextMap.put(id, value);
    }

    public void addToContext(ResourceContextId id, Map<String, String> map) {
        mapContextMap.put(id, map);
    }

    public String getFromContext(ResourceContextId id) {
        return stringContextMap.get(id);
    }

    public Map<String, String> getMapFromContext(ResourceContextId id) {
        return mapContextMap.get(id);
    }

    protected T populate(T bean, Form form) {
        try {

            beanUtilsBean.populate(bean, form.getValuesMap());
            return bean;
        } catch (Exception e) {
            log.error("Error populating bean {} from form {}", bean, form.getValuesMap(), e);
            return null;
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
        List<Field> inheritedFields = getInheritedFields(getParameterType());
        return inheritedFields.stream().map(f -> f.getName()).collect(Collectors.toList());
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
