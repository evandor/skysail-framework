package io.skysail.server.restlet.resources;

import io.skysail.api.documentation.API;
import io.skysail.api.links.LinkRelation;
import io.skysail.api.responses.FormResponse;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.RequestHandler;
import io.skysail.server.restlet.filter.AbstractResourceFilter;
import io.skysail.server.services.PerformanceTimer;

import java.text.ParseException;
import java.util.Set;

import javax.validation.ConstraintValidatorFactory;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import de.twenty11.skysail.server.core.restlet.ResponseWrapper;

/**
 * Abstract base class for skysail server-side resources representing a single
 * entity with known id.
 *
 * <p>
 * This class takes care of GET-, PUT- and DELETE-Requests routed to this
 * specific resource.
 * </p>
 *
 * <p>
 * Example usage:
 * </p>
 *
 * <pre>
 *  <code>
 *  public class MyEntityResource extends EntityServerResource&lt;MyEntity&gt; {
 * 
 *     private MyApplication app;
 *     private String myEntityId;
 * 
 *     protected void doInit() {
 *         myEntityId = getAttribute("id");
 *         app = (MyApplication) getApplication();
 *     }
 * 
 *     public MyEntity getEntity() {
 *         return app.getRepository().getById(MyEntity.class, myEntityId);
 *     }
 * 
 *     public List&lt;Link&gt; getLinks() {
 *          return super.getLinkheader(PutMyEntityResource.class);
 *     }
 *     
 *     public SkysailResponse<?> eraseEntity() {
 *         return null;
 *     }
 * 
 * }
 * </code>
 * </pre>
 *
 * @param <T>
 *            the entity
 */
public abstract class EntityServerResource<T> extends SkysailServerResource<T> {

    private static final Logger logger = LoggerFactory.getLogger(EntityServerResource.class);

    public EntityServerResource() {
        addToContext(ResourceContextId.LINK_TITLE, "show");
    }

    /**
     * If you have a route defined as "/somepath/{key}/whatever", you can get
     * the key like this: key = getAttribute("key");
     *
     * <p>
     * To get hold on any parameters passed, consider using this pattern:
     * </p>
     *
     * <p>
     * <code>Form form = new Form(getRequest().getEntity()); action =
     * form.getFirstValue("action");</code>
     * </p>
     *
     */
    @Override
    protected void doInit() {
        // this empty implementation provides the comment to subclasses only.
    };

    public String getId() {
        return null;
    }

    /**
     * will be called in case of a DELETE request. Override in subclasses if
     * they support DELETE requests.
     *
     * @return the response
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
     *             parse exception
     */
    public T getData(Form form) throws ParseException {
        logger.warn("trying to read data from form {}, but getData(Form form) is not overwritten by {}", form, this
                .getClass().getName());
        return null;

    }

    @Override
    public LinkRelation getLinkRelation() {
        return LinkRelation.ITEM;
    }

    // input: html|json|..., output: html|json|...
    /**
     * @return the response
     */
    @Get("html|json|eventstream|treeform|txt|csv|yaml|mailto")
    @API(desc = "retrieves the entity defined by the url")
    public SkysailResponse<T> getEntity2() {
        Set<PerformanceTimer> perfTimer = getApplication().startPerformanceMonitoring(this.getClass().getSimpleName() + ":getEntity");
        logger.info("Request entry point: {} @Get('html|json|eventstream|treeform|txt')", this.getClass().getSimpleName());
        T entity = getEntity3();
        getApplication().stopPerformanceMonitoring(perfTimer);
        return new SkysailResponse<T>(entity);
    }
    
    @Get("htmlform")
    @API(desc = "provides a form to delete the entity")
    public SkysailResponse<T> getDeleteForm() {
        return new FormResponse<T>(getEntity("dummy"), ".", "/");
    }

    @Delete("x-www-form-urlencoded:html|html|json")
    @API(desc = "deletes the entity defined in the url")
    public T deleteEntity() {
        Set<PerformanceTimer> perfTimer = getApplication().startPerformanceMonitoring(this.getClass().getSimpleName() + ":deleteEntity");
        logger.info("Request entry point: {} @Delete('x-www-form-urlencoded:html|html|json')", this.getClass()
                .getSimpleName());

        RequestHandler<T> requestHandler = new RequestHandler<T>(getApplication());
        AbstractResourceFilter<EntityServerResource<T>, T> handler = requestHandler.createForEntity(Method.DELETE);
        T entity = handler.handle(this, getResponse()).getEntity();
        getApplication().stopPerformanceMonitoring(perfTimer);
        return entity;
    }
    
    protected T getEntity3() {
        RequestHandler<T> requestHandler = new RequestHandler<T>(getApplication());
        AbstractResourceFilter<EntityServerResource<T>, T> chain = requestHandler.createForEntity(Method.GET);
        ResponseWrapper<T> wrapper = chain.handle(this, getResponse());
        return wrapper.getEntity();
    }
    
    protected String getDataAsJson() {
        return "    ";
    }

    public Validator getValidator() {
        return null;
    }

    protected Set<ConstraintViolation<T>> validate(T entity) {
        throw new UnsupportedOperationException();
    }

    protected ConstraintValidatorFactory getConstraintValidatorFactory() {
        return null;
    }

}
