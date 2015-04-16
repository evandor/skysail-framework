package io.skysail.server.restlet.resources;

import io.skysail.api.documentation.API;
import io.skysail.api.domain.Identifiable;
import io.skysail.api.links.Link;
import io.skysail.api.links.LinkRelation;
import io.skysail.api.responses.FormResponse;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.RequestHandler;
import io.skysail.server.restlet.filter.AbstractResourceFilter;
import io.skysail.server.services.PerformanceTimer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import lombok.extern.slf4j.Slf4j;

import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.representation.Variant;
import org.restlet.resource.Get;
import org.restlet.resource.Patch;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import de.twenty11.skysail.server.core.restlet.ResponseWrapper;

/**
 * An abstract resource template dealing with PUT requests (see
 * http://www.ietf.org/rfc/rfc2616.txt, 9.6).
 * 
 * <p>
 * Process:
 * </p>
 * 
 * <p>
 * Typically, you'd implement doInit to get hold on the id, then getEntity to
 * retrieve the entitiy to be updated, and finally updateEntity to persist it.
 * </p>
 * 
 * Example implementing class:
 * 
 * <pre>
 *  <code>
 *  public class MyEntityResource extends PutEntityServerResource&lt;MyEntity&gt; {
 * 
 *     ...
 *     
 *     protected void doInit() throws ResourceException {
 *       super.doInit();
 *       id = getAttribute("id");
 *       app = (MyApplication) getApplication();
 *     }
 *     
 *     public T getEntity() {
 *        return app.getRepository().getById(id)
 *     }
 *     
 *     public SkysailResponse<?> updateEntity(T entity) {
 *        app.getRepository().update(entity);
 *        return new SkysailResponse<String>();
 *     }
 * }
 * </code>
 * </pre>
 * 
 * @param <T>
 */
@Slf4j
public abstract class PutEntityServerResource<T> extends SkysailServerResource<T> {

    private String identifierName;
    private String identifier;

    public PutEntityServerResource() {
        this("id");
    }

    public PutEntityServerResource(String identifierName) {
        this.identifierName = identifierName;
        addToContext(ResourceContextId.LINK_TITLE, "update");
    }

    /**
     * If you have a route defined as "/repository/{key}", you can get the key
     * like this: key = (String) getRequest().getAttributes().get("key");
     * 
     * <p>
     * To get hold on any parameters passed, consider using this pattern:
     * </p>
     *
     * <p>
     * Form form = new Form(getRequest().getEntity()); action =
     * form.getFirstValue("action");
     * </p>
     * 
     */
    @Override
    protected void doInit() throws ResourceException {
        identifier = getAttribute(identifierName);
    }

    /**
     * will be called in case of a PUT request.
     */
    public abstract SkysailResponse<?> updateEntity(T entity);

    /**
     * This method will be called by the skysail framework to create the actual
     * resource from its form representation.
     * 
     * @param form
     *            the representation of the resource as a form
     * @return the resource of type T
     */
    public T getData(Form form) {
        return populate(getEntity(), form);
    }

    @Get("htmlform|html|json")
    @API(desc = "create an html form with the current entity to be updated")
    public SkysailResponse<T> createForm(Variant variant) {
        Set<PerformanceTimer> perfTimer = getApplication().startPerformanceMonitoring("PutEntityServerResource:createForm");
        log.info("Request entry point: {} @Get('htmlform|html|json') createForm with variant {}",
                PutEntityServerResource.class.getSimpleName(), variant);

        RequestHandler<T> requestHandler = new RequestHandler<T>(getApplication());
        AbstractResourceFilter<PutEntityServerResource<T>, T> chain = requestHandler.createForFormResponse();
        ResponseWrapper<T> wrapper = chain.handle(this, getResponse());
        
        getApplication().stopPerformanceMonitoring(perfTimer);
        return new FormResponse<T>(wrapper.getEntity(), identifier, ".", redirectBackTo());
    }

    protected String redirectBackTo() {
        return null;
    }

    @Put("json")
    @API(desc = "generic PUT for JSON")
    public Object putEntity(T entity) {
        Set<PerformanceTimer> perfTimer = getApplication().startPerformanceMonitoring("PutEntityServerResource:putEntity");
        log.info("Request entry point: {} @Put('json')", this.getClass().getSimpleName());
        getRequest().getAttributes().put(SKYSAIL_SERVER_RESTLET_ENTITY, entity);
        Object put = put((Form) null);
        getApplication().stopPerformanceMonitoring(perfTimer);
        return put;
    }

    @Patch("json")
    @API(desc = "generic Patch for JSON")
    public Object patchEntity(T entity) {
        Set<PerformanceTimer> perfTimer = getApplication().startPerformanceMonitoring("PutEntityServerResource:patchEntity");
        log.info("Request entry point: {} @Patch('json')", this.getClass().getSimpleName());
        getRequest().getAttributes().put(SKYSAIL_SERVER_RESTLET_ENTITY, entity);
        Object patch = put((Form) null);
        getApplication().stopPerformanceMonitoring(perfTimer);
        return patch;
    }

    @Put("x-www-form-urlencoded:html|json")
    @API(desc = "generic PUT for x-www-form-urlencoded")
    public Object put(Form form) {
        Set<PerformanceTimer> perfTimer = getApplication().startPerformanceMonitoring("PutEntityServerResource:put");
        log.info("Request entry point: {} @Put('x-www-form-urlencoded:html|json')", this.getClass().getSimpleName());
        if (form != null) {
            getRequest().getAttributes().put(SKYSAIL_SERVER_RESTLET_FORM, form);
        }
        RequestHandler<T> requestHandler = new RequestHandler<T>(getApplication());
        AbstractResourceFilter<PutEntityServerResource<T>, T> handler = requestHandler.createForPut();
        ResponseWrapper<T> handledRequest = handler.handle(this, getResponse());
        getApplication().stopPerformanceMonitoring(perfTimer);
        if (handledRequest.getConstraintViolationsResponse() != null) {
            return handledRequest.getConstraintViolationsResponse();
        }
        return handledRequest.getEntity();
    }

    @Override
    public LinkRelation getLinkRelation() {
        return LinkRelation.CREATE_FORM;
    }

    protected Set<ConstraintViolation<T>> validate(T entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Link> getLinkheader() {
        
        String ref = getReference().toString();
        String parentRef = getReference().getParentRef().toString();
        
        List<Link> result = new ArrayList<>();
        result.add(new Link.Builder(ref).relation(LinkRelation.NEXT).title("form target").verbs(Method.PUT).build());

        T entity = this.getCurrentEntity();
        if (entity instanceof Identifiable) {
            String id = ((Identifiable)entity).getId().replace("#","");
            
            result.add(new Link.Builder(parentRef + id).title("delete").verbs(Method.DELETE).build());
        }

        return result;

    }
}
