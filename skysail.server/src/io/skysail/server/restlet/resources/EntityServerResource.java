package io.skysail.server.restlet.resources;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.links.LinkRelation;
import io.skysail.api.responses.*;
import io.skysail.server.restlet.RequestHandler;
import io.skysail.server.restlet.filter.AbstractResourceFilter;
import io.skysail.server.services.PerformanceTimer;

import java.text.ParseException;
import java.util.Set;

import javax.validation.*;

import org.restlet.data.*;
import org.restlet.representation.Variant;
import org.restlet.resource.*;
import org.slf4j.*;

import de.twenty11.skysail.server.core.restlet.*;

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
 *     public SkysailResponse&lt;?&gt; eraseEntity() {
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
public abstract class EntityServerResource<T extends Identifiable> extends SkysailServerResource<T> {

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
        super.doInit();
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
    public EntityServerResponse<T> getEntity2(Variant variant) {
        Set<PerformanceTimer> perfTimer = getApplication().startPerformanceMonitoring(this.getClass().getSimpleName() + ":getEntity");
        logger.info("Request entry point: {} @Get('html|json|eventstream|treeform|txt')", this.getClass().getSimpleName());
        if (variant != null) {
            getRequest().getAttributes().put(SKYSAIL_SERVER_RESTLET_VARIANT, variant);
        }
        T entity = getEntity3();
        getApplication().stopPerformanceMonitoring(perfTimer);
        return new EntityServerResponse<>(entity);
    }

    @Get("htmlform")
    public SkysailResponse<T> getDeleteForm() {
        return new FormResponse<T>(getEntity("dummy"), ".", "/");
    }

    @Delete("x-www-form-urlencoded:html|html|json")
    public EntityServerResponse<T> deleteEntity(Variant variant) {
        Set<PerformanceTimer> perfTimer = getApplication().startPerformanceMonitoring(this.getClass().getSimpleName() + ":deleteEntity");
        logger.info("Request entry point: {} @Delete('x-www-form-urlencoded:html|html|json')", this.getClass()
                .getSimpleName());
        if (variant != null) {
            getRequest().getAttributes().put(SKYSAIL_SERVER_RESTLET_VARIANT, variant);
        }
        RequestHandler<T> requestHandler = new RequestHandler<T>(getApplication());
        AbstractResourceFilter<EntityServerResource<T>, T> handler = requestHandler.createForEntity(Method.DELETE);
        T entity = handler.handle(this, getResponse()).getEntity();
        getApplication().stopPerformanceMonitoring(perfTimer);
        return new EntityServerResponse<>(entity);
    }

//    @Options("json")
//    public final SkysailResponse<ResourceContextResource> doOptions(Representation entity, Variant variant) {
//        Set<PerformanceTimer> perfTimer = getApplication().startPerformanceMonitoring(
//                this.getClass().getSimpleName() + ":doOptions");
//        log.info("Request entry point: {}  @Options('json') with variant {}", this.getClass().getSimpleName(),
//                variant);
//        ResourceContextResource context = new ResourceContextResource(this);
//        getApplication().stopPerformanceMonitoring(perfTimer);
//        return new SkysailResponse<ResourceContextResource>(context);
//    }


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
