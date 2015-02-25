package de.twenty11.skysail.server.core.restlet;

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

import lombok.NonNull;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.shiro.SecurityUtils;
import org.restlet.Application;
import org.restlet.data.Form;
import org.restlet.data.Reference;
import org.restlet.resource.ServerResource;
import org.restlet.security.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.twenty11.skysail.api.responses.LinkHeaderRelation;
import de.twenty11.skysail.api.responses.Linkheader;
import de.twenty11.skysail.server.app.SkysailApplication;
import de.twenty11.skysail.server.app.TranslationProvider;
import de.twenty11.skysail.server.core.FormField;
import de.twenty11.skysail.server.core.restlet.filter.AbstractResourceFilter;
import etm.core.configuration.EtmManager;
import etm.core.monitor.EtmMonitor;

/**
 * Abstract base class for all skysail resources, parameterized with T, the type
 * of the entity handled.
 * 
 * Subclasses should override the methods
 * SkysailServerResource#addEntity(Object),
 * SkysailServerResource#updateEntity(Object) and/or
 * SkysailServerResource#eraseEntity() if they support adding, updating and/or
 * deletion of the referenced resource. It is assumed that
 * SkysailServerResources will always support GET requests, which are dealt with
 * by the abstract method @link {@link SkysailServerResource#getData()}.
 * 
 * Those methods are called from the framework, see {@link RequestHandler} and
 * the various {@link AbstractResourceFilter} implementations. The
 * RequestHandler provides a chain of filters (depending on the current request)
 * which will make sure that aspects like error management, logging, security
 * issues and the like are always dealt with in the same way.
 * 
 */
public abstract class SkysailServerResource<T> extends ServerResource {

    private static final Logger logger = LoggerFactory.getLogger(SkysailServerResource.class);

    public static final String ATTRIBUTES_INTERNAL_REQUEST_ID = "de.twenty11.skysail.server.restlet.SkysailServerResource.requestId";

    protected static final EtmMonitor etmMonitor = EtmManager.getEtmMonitor();

    /** the payload. */
    private T skysailData;

    private String desc;

    private Map<ResourceContextId, String> stringContextMap = new HashMap<>();

    private Map<ResourceContextId, Map<String, String>> mapContextMap = new HashMap<>();

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
    public abstract T getData();

    /**
     * @return the type of relation this resource represents, e.g. LIST, ITEM,
     *         ...
     */
    public abstract LinkHeaderRelation getLinkRelation();

    /**
     * get Messages.
     * 
     * @return map with messages
     */
    public Map<String, String> getMessages() {
        Application application = getApplication();
        if (!(application instanceof TranslationProvider)) {
            return Collections.emptyMap();
        }
        Map<String, String> msgs = new HashMap<>();
        msgs.put("content.header",
                "default msg from de.twenty11.skysail.server.core.restlet.SkysailServerResource.getMessages()");
        String key = getClass().getName() + ".message";
        String translated = ((TranslationProvider) application).translate(key, key, this, true);
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
        if (!(application instanceof TranslationProvider)) {
            return msgs;
        }
        fields.stream().forEach(f -> {
            Class<? extends Object> entityClass = f.getEntity().getClass();
            String baseKey = MessagesUtils.getBaseKey(entityClass, f);
            addTranslation(msgs, application, f, baseKey);
            addTranslation(msgs, application, f, baseKey + ".desc");
            addTranslation(msgs, application, f, baseKey + ".placeholder");
        });

        return msgs;
    }

    private void addTranslation(Map<String, String> msgs, Application application, FormField f, String key) {
        String defaultMsg = MessagesUtils.getSimpleName(f);
        String translation = ((TranslationProvider) application).translate(key, defaultMsg, this, false);
        if (translation != null) {
            msgs.put(key, translation);
        } else {
            msgs.put(key, key);
        }
    }

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
     * get Linkheader.
     * 
     * @param classes
     *            the classes
     * @return linkheader the linkheader
     */
    @SafeVarargs
    public final List<Linkheader> getLinkheader(Class<? extends SkysailServerResource<?>>... classes) {
        SkysailApplication app = getApplication();
        List<Linkheader> linkheader = Arrays.asList(classes).stream() //
                .map(cls -> ServerLink.fromResource(app, cls))//
                .filter(lh -> {
                    return lh != null;
                }).collect(Collectors.toList());
        linkheader.forEach(getPathSubstitutions());
        return linkheader;
    }

    /**
     * example: l -&gt; { l.substitute("spaceId", spaceId).substitute("id",
     * getData().getPage().getRid()); };
     *
     * @return consumer for pathSubs
     */
    public Consumer<? super Linkheader> getPathSubstitutions() {
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
    public List<Linkheader> getLinkheader() {
        return new ArrayList<Linkheader>();
    }

    /**
     * Links might be removed from the framework if the current user isn't
     * authorized to call them.
     * 
     * @return result
     */
    public List<Linkheader> getLinkheaderAuthorized() {
        List<Linkheader> allLinks = getLinkheader();
        return allLinks.stream().filter(link -> isAuthorized(link)).collect(Collectors.toList());
    }

    private boolean isAuthorized(@NonNull Linkheader link) {
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

    public T getSkysailData() {
        return skysailData;
    }

    public void setSkysailData(T skysailData) {
        this.skysailData = skysailData;
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
        Linkheader linkheader = ServerLink.fromResource(app, cls);
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
            DateConverter dateConverter = new DateConverter(null);
            dateConverter.setPattern("yyyy-MM-dd");
            ConvertUtils.register(dateConverter, Date.class);
            BeanUtils.populate(bean, form.getValuesMap());
            return bean;
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    protected Map<String, String> describe(T bean) {
        try {
            return BeanUtils.describe(bean);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            logger.error(e.getMessage(), e);
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
