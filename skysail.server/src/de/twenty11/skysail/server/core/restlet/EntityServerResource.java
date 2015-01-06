package de.twenty11.skysail.server.core.restlet;

import java.text.ParseException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidatorFactory;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.restlet.Response;
import org.restlet.data.ClientInfo;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Preference;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.api.responses.FormResponse;
import de.twenty11.skysail.api.responses.LinkHeaderRelation;
import de.twenty11.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.filter.AbstractResourceFilter;
import etm.core.monitor.EtmPoint;

/**
 * Abstract base class for skysail server-side resources representing a single
 * entity with known id.
 *
 * This class takes care of GET-, PUT- and DELETE-Requests routed to this
 * specific resource.
 *
 * Example usage:
 *
 * <pre>
 *  <code>
 *  public class MyEntityResource extends EntityServerResource<MyEntity> {
 * 
 *     private MyApplication app;
 *     private String myEntityId;
 * 
 *     public MyEntityResource() {
 *        app = (MyApplication) getApplication();
 *     }
 * 
 *     // if you have "router.attach(new RouteBuilder("/myentity/{id}", ...)" in your SkysailAppliation
 *     protected void doInit() throws ResourceException {
 *         myEntityId = getAttribute("id");
 *     }
 * 
 * 
 *     public MyEntity getData() {
 *         return app.getMyEntityRepository().getById(myEntityId);
 *     }
 * 
 * 
 * }
 * </code>
 * </pre>
 *
 * @param <T>
 */
public abstract class EntityServerResource<T> extends SkysailServerResource<T> {

    public static final String SKYSAIL_SERVER_RESTLET_FORM = "de.twenty11.skysail.server.core.restlet.form";
    public static final String SKYSAIL_SERVER_RESTLET_ENTITY = "de.twenty11.skysail.server.core.restlet.entity";

    private static final Logger logger = LoggerFactory.getLogger(EntityServerResource.class);
    private Class<? extends ListServerResource<?>> associatedResource;

    public EntityServerResource() {
    	addToContext(ResourceContextId.LINK_TITLE, "show");
    }

    public EntityServerResource(Class<? extends ListServerResource<?>> associatedResource) {
    	this();
        this.associatedResource = associatedResource;
    }

    /**
     * If you have a route defined as "/somepath/{key}/whatever", you can get
     * the key like this: key = getAttribute("key");
     *
     * To get hold on any parameters passed, consider using this pattern:
     *
     * Form form = new Form(getRequest().getEntity()); action =
     * form.getFirstValue("action");
     *
     */
    @Override
    protected void doInit() throws ResourceException {
        // this empty implementation provides the comment to subclasses only.
    };

    /**
     * the representation data of this resource.
     */
    @Override
    public abstract T getData();
    
    public abstract String getId();
    
    /**
     * will be called in case of a DELETE request. Override in subclasses if
     * they support DELETE requests.
     */
    public abstract SkysailResponse<?> eraseEntity();

    /**
     * This method will be called by the skysail framework to create the actual
     * resource from its form representation.
     * 
     * @param form
     *            the representation of the resource as a form
     * @return the resource of type T
     * @throws ParseException
     */
    public T getData(Form form) throws ParseException {
        logger.warn("trying to read data from form {}, but getData(Form form) is not overwritten by {}", form, this
                .getClass().getName());
        return null;

    }

    @Override
    public LinkHeaderRelation getLinkRelation() {
        return LinkHeaderRelation.ITEM;
    }

    @Get("json")
    public T getJson() {
    	EtmPoint point = etmMonitor.createPoint("EntityServerResource:getJson");
        logger.info("Request entry point: {} @Get('json')", this.getClass().getSimpleName());
        logger.info(scoringInfo(getRequest().getClientInfo()));
        point.collect();
        return getEntity("dummy");
    }

    private String scoringInfo(ClientInfo clientInfo) {
        List<Preference<MediaType>> acceptedMediaTypes = clientInfo.getAcceptedMediaTypes();
        StringBuilder sb = new StringBuilder("AcceptedMediaTypes: ");
        sb.append(acceptedMediaTypes.stream().map(amt -> amt.toString()).collect(Collectors.joining(",")));
        return sb.toString();
    }

    // input: html|json|..., output: html|json|...
    @Get("html|eventstream|treeform|txt")
    public SkysailResponse<T> getEntity() {
    	EtmPoint point = etmMonitor.createPoint("EntityServerResource:getEntity");
        logger.info("Request entry point: {} @Get('html|eventstream|treeform|txt')", this.getClass().getSimpleName());
        logger.info(scoringInfo(getRequest().getClientInfo()));
        T entity = getEntity("dummy");
        point.collect();
        return new SkysailResponse<T>(entity);
    }
    
    @Get("htmlform")
    public SkysailResponse<T> getDeleteForm() {
    	return new FormResponse<T>(getEntity("dummy"), ".", "/");
    }

    @Delete("html|json")
    public T deleteEntity() {
    	EtmPoint point = etmMonitor.createPoint("EntityServerResource:deleteEntity");
        logger.info("Request entry point: {} @Delete('html|json')", this.getClass().getSimpleName());

        // getRequest().getAttributes().put(SKYSAIL_SERVER_RESTLET_ENTITY,
        // entity);
        RequestHandler<T> requestHandler = new RequestHandler<T>(getApplication());
        AbstractResourceFilter<EntityServerResource<T>, T> handler = requestHandler.createForEntity(Method.DELETE,
                getConstraintValidatorFactory());
         T entity = handler.handle(this, getResponse()).getEntity();
         point.collect();
         return entity;
    }

    protected T getEntity(String defaultMsg) {
        RequestHandler<T> requestHandler = new RequestHandler<T>(getApplication());
        AbstractResourceFilter<EntityServerResource<T>, T> chain = requestHandler.createForEntity(Method.GET, null);
        ResponseWrapper<T> wrapper = chain.handle(this, getResponse());
        return wrapper.getEntity();
    }

    /**
     * We have cases where we can retrieve JSON representations "early", for example when using a noSQL database. 
     * In this case, we don't want to create objects of type T and then let them converted back to JSON by the
     * JacksonConverter.
     */
    protected String getEntityAsJson() {
        RequestHandler<String> requestHandler = new RequestHandler<String>(getApplication());
        AbstractResourceFilter<EntityServerResource<String>, String> chain = requestHandler.createForEntity(Method.GET, null);
        EntityServerResource<String> resource = new EntityServerResource<String>() {

            @Override
            public String getData() {
                return EntityServerResource.this.getDataAsJson();
            }

            @Override
            public String getId() {
                return EntityServerResource.this.getId();
            }

            @Override
            public SkysailResponse<?> eraseEntity() {
                throw new IllegalAccessError("this method is not supposed to be called in this context");
            }
            @Override
            public Response getResponse() {
                return EntityServerResource.this.getResponse();
            }
        };
        ResponseWrapper<String> wrapper = chain.handle(resource, getResponse());
        return wrapper.getEntity();
    }

    protected String getDataAsJson() {
        return "overwrite in subclass";
    }

    public Validator getValidator() {
        return null;
    }

    public Class<? extends ListServerResource<?>> getAssociatedListServerResource() {
        return associatedResource;
    }
    
    protected Set<ConstraintViolation<T>> validate(@SuppressWarnings("unused") T entity) {
        throw new UnsupportedOperationException();
    }

    protected ConstraintValidatorFactory getConstraintValidatorFactory() {
        return null;
    }

}
