package io.skysail.server.restlet.resources;

import io.skysail.api.documentation.API;
import io.skysail.api.links.Link;
import io.skysail.api.links.LinkRelation;
import io.skysail.api.responses.FormResponse;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.RequestHandler;
import io.skysail.server.restlet.filter.AbstractResourceFilter;
import io.skysail.server.restlet.filter.CheckBusinessViolationsFilter;
import io.skysail.server.restlet.filter.FormDataExtractingFilter;
import io.skysail.server.services.PerformanceTimer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;

import lombok.extern.slf4j.Slf4j;

import org.apache.shiro.SecurityUtils;
import org.restlet.data.ClientInfo;
import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import de.twenty11.skysail.server.core.restlet.ResponseWrapper;
import de.twenty11.skysail.server.services.SearchService;

/**
 * An abstract resource template dealing with POST requests (see
 * http://www.ietf.org/rfc/rfc2616.txt, 9.5).
 * 
 * Process:
 * 
 * Restlet framework will call
 * de.twenty11.skysail.server.core.restlet.PostEntityServerResource.post(Form),
 * where a responseHandler for a post request is created. This response handler
 * will use a couple of filters to process the request. The
 * {@link FormDataExtractingFilter} will call back to the implementing class
 * (#getData(Form form)) and attach the result to the skysail response data
 * field. Afterwards, the {@link CheckBusinessViolationsFilter} will validate
 * the provided data.
 * 
 * Example implementing class:
 * 
 * <pre>
 *  <code>
 *  public class MyEntityResource extends PostEntityServerResource&lt;MyEntity&gt; {
 * 
 *     private MyApplication app;
 *     private String myEntityId;
 * 
 *     public void doInit() {
 *        app = (MyApplication) getApplication();
 *     }
 *     
 *     public MyEntity createEntityTemplate() {
 *         return new MyEntity();
 *     }
 *    
 *     public MyEntity getData(Form form) {
 *         return populate(createEntityTemplate(), form);
 *     }
 *    
 *    public SkysailResponse&lt;?&gt; addEntity(Clip entity) {
 *        app.getClipsRepository().add(entity);
 *        return new SkysailResponse&lt;String&gt;();
 *    }
 * 
 * }
 * </code>
 * </pre>
 * 
 * @param <T>
 */
@Slf4j
public abstract class PostEntityServerResource<T> extends SkysailServerResource<T> {

    /** the value of the submit button */
    protected String submitValue;

    /**
     * If you have a route defined as "/repository/{key}", you can get the key
     * like this: key = (String) getRequest().getAttributes().get("key");
     *
     * <p>To get hold on any parameters passed, consider using this pattern:</p>
     *
     * <p>Form form = new Form(getRequest().getEntity()); action =
     * form.getFirstValue("action");</p>
     * 
     */
    @Override
    protected void doInit() throws ResourceException {
        // empty
    };

    public PostEntityServerResource() {
        addToContext(ResourceContextId.LINK_TITLE, "create");
        addToContext(ResourceContextId.LINK_GLYPH, "plus");
    }

    /**
     * The concrete resource should provide a template (a potentially non-valid
     * instance of type T) which will be passed to the client to indicate the
     * structure the server expects, e.g. if the client asks for a json
     * representation of a user resource, this method could return a new User()
     * object which will be serialized as json (with all fields null), so that
     * the client knows about the attributes and could provide a generic input
     * form.
     * 
     * @return a template instance of type T
     */
    public abstract T createEntityTemplate();

    /**
     * will be called in case of a POST request.
     * 
     * @param entity
     *            the entity
     * @return the response
     */
    public abstract SkysailResponse<?> addEntity(T entity);

    @Override
    public T getEntity() {
        return createEntityTemplate();
    }

    /**
     * This method will be called by the skysail framework to create the actual
     * resource from its form representation.
     * 
     * @param form
     *            the representation of the resource as a form
     * @return the resource of type T
     */
    public T getData(Form form) {
        submitValue = form.getFirstValue("submit");
        return populate(createEntityTemplate(), form);
    };

    @Get("htmlform|html")
    @API(desc = "create an html form to post a new entity")
    public SkysailResponse<T> createForm() {
        log.info("Request entry point: {} @Get('htmlform|html')", this.getClass().getSimpleName());
        return new FormResponse<T>(createEntityTemplate(), ".");
    }

    @Get("json")
    @API(desc = "as Json")
    public T getJson() {
        Set<PerformanceTimer> perfTimer = getApplication().startPerformanceMonitoring(this.getClass().getSimpleName() + ":getJson");
        log.info("Request entry point: {} @Get('json')", this.getClass().getSimpleName());
        RequestHandler<T> requestHandler = new RequestHandler<T>(getApplication());
        AbstractResourceFilter<PostEntityServerResource<T>, T> handler = requestHandler.newInstance(Method.GET);
        T entity = handler.handle(this, getResponse()).getEntity();
        getApplication().stopPerformanceMonitoring(perfTimer);
        return entity;
    }

    /**
     * 
     * @param entity
     * @return
     */
    @Post("json")
    @API(desc = "generic POST for JSON")
    public Object post(T entity) {
        Set<PerformanceTimer> perfTimer = getApplication().startPerformanceMonitoring(this.getClass().getSimpleName() + ":post");
        log.info("Request entry point: {} @Post('json')", this.getClass().getSimpleName());
        if (entity != null) {
            getRequest().getAttributes().put(SKYSAIL_SERVER_RESTLET_ENTITY, entity);
        } else {
            log.warn("provided entity was null!");
        }

        Object post = post((Form) null);
        getApplication().stopPerformanceMonitoring(perfTimer);
        return post;
    }

    /**
     * handles a x-www-form-urlencoded POST to this resource.
     * 
     * @param form
     * @return
     */
    @Post("x-www-form-urlencoded:html")
    @API(desc = "generic POST for x-www-form-urlencoded")
    public Object post(Form form) {
        Set<PerformanceTimer> perfTimer = getApplication().startPerformanceMonitoring(this.getClass().getSimpleName() + ":postForm");
        ResponseWrapper<T> handledRequest = doPost(form);
        getApplication().stopPerformanceMonitoring(perfTimer);
        if (handledRequest.getConstraintViolationsResponse() != null) {
            return handledRequest.getConstraintViolationsResponse();
        }
        return handledRequest.getEntity();
    }

    private ResponseWrapper<T> doPost(Form form) {
        log.info("Request entry point: {} @Post('x-www-form-urlencoded:html|json|xml')", this.getClass()
                .getSimpleName());
        ClientInfo ci = getRequest().getClientInfo();
        log.info("calling post(Form), media types '{}'", ci != null ? ci.getAcceptedMediaTypes() : "test");
        if (form != null) {
            getRequest().getAttributes().put(SKYSAIL_SERVER_RESTLET_FORM, form);
        }
        RequestHandler<T> requestHandler = new RequestHandler<T>(getApplication());
        AbstractResourceFilter<PostEntityServerResource<T>, T> handler = requestHandler.createForPost();
        getResponse().setStatus(Status.SUCCESS_CREATED);
        ResponseWrapper<T> handledRequest = handler.handle(this, getResponse());
        return handledRequest;
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
        return Arrays.asList(new Link.Builder(".").relation(LinkRelation.NEXT).title("form target").verbs(Method.POST)
                .build());
    }
    
    /**
     * String id = entity.getRid().toString().replace("#",""); String link =
     * ServerLink.fromResource(app, ClipResource.class).getUri();
     *
     * @param entity
     *            the entity
     * @param searchService
     *            a search service
     * @param link
     *            the link
     * @param id
     *            the id
     */
    protected void index(T entity, SearchService searchService, String link, String id) {
        if (searchService == null) {
            log.warn("no search service available - document will not be indexed");
            return;
        }
        try {
            Map<String, String> getMap = describe(entity);
            getMap.put("_owner", SecurityUtils.getSubject().getPrincipal().toString());

            // String link = ServerLink.fromResource(app,
            // ClipResource.class).getUri();// + "/" + entity.getRid();
            getPathSubstitutions();
            link = link.replace("{id}", id);
            getMap.put("_link", link);
            searchService.addDocument(getMap);
        } catch (IOException e) {
            log.error("error indexing document", e);
        }

    }

}
