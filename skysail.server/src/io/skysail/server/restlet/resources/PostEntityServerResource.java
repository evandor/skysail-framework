package io.skysail.server.restlet.resources;

import java.io.IOException;
import java.util.*;

import javax.validation.ConstraintViolation;

import org.apache.shiro.SecurityUtils;
import org.restlet.data.*;
import org.restlet.data.Status;
import org.restlet.representation.Variant;
import org.restlet.resource.*;

import com.tinkerpop.blueprints.impls.orient.OrientVertex;

import de.twenty11.skysail.server.core.restlet.*;
import io.skysail.api.domain.Identifiable;
import io.skysail.api.links.*;
import io.skysail.api.responses.*;
import io.skysail.api.search.SearchService;
import io.skysail.server.restlet.RequestHandler;
import io.skysail.server.restlet.filter.*;
import io.skysail.server.services.PerformanceTimer;
import lombok.extern.slf4j.Slf4j;

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
public abstract class PostEntityServerResource<T extends Identifiable> extends SkysailServerResource<T> {

    /** the value of the submit button */
    protected String submitValue;

    public PostEntityServerResource() {
        addToContext(ResourceContextId.LINK_TITLE, "create");
        addToContext(ResourceContextId.LINK_GLYPH, "plus");
    }

    /**
     * The concrete resource should provide a template (a potentially non-valid
     * instance of type T).
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
    public void addEntity(T entity) {
        Class<? extends Identifiable> cls = createEntityTemplate().getClass();
        OrientVertex vertex = (OrientVertex) getApplication().getRepository(cls).save(entity);
        String id = vertex.getId().toString();
        entity.setId(id);
    }

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
        T entity = createEntityTemplate();
        this.setCurrentEntity(entity);
        return populate(entity, form);
    }

    @Get("htmlform|html")
    public SkysailResponse<T> createForm() {
        log.info("Request entry point: {} @Get('htmlform|html')", this.getClass().getSimpleName());
        List<String> templatePaths = getApplication().getTemplatePaths(this.getClass());
        String formTarget = templatePaths.stream().findFirst().orElse(".");
        List<Link> links = Arrays.asList(new Link.Builder(formTarget).build());
        links.stream().forEach(getPathSubstitutions());

        T entity = createEntityTemplate();
        this.setCurrentEntity(entity);
        return new FormResponse<T>(getResponse(), entity, links.get(0).getUri());
    }

    @Get("json")
    public T getJson() {
        Set<PerformanceTimer> perfTimer = getApplication().startPerformanceMonitoring(this.getClass().getSimpleName() + ":getJson");
        log.info("Request entry point: {} @Get('json')", this.getClass().getSimpleName());
        RequestHandler<T> requestHandler = new RequestHandler<T>(getApplication());
        AbstractResourceFilter<PostEntityServerResource<T>, T> handler = requestHandler.newInstance(Method.GET);
        T entity = handler.handle(this, getResponse()).getEntity();
        getApplication().stopPerformanceMonitoring(perfTimer);
        return entity;
    }

    @Post("json")
    public SkysailResponse<T> post(T entity, Variant variant) {
        Set<PerformanceTimer> perfTimer = getApplication().startPerformanceMonitoring(this.getClass().getSimpleName() + ":post");
        log.info("Request entry point: {} @Post('json')", this.getClass().getSimpleName());
        if (entity != null) {
            getRequest().getAttributes().put(SKYSAIL_SERVER_RESTLET_ENTITY, entity);
        } else {
            log.warn("provided entity was null!");
        }

        SkysailResponse<T> post = post((Form) null, variant);
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
    public SkysailResponse<T> post(Form form, Variant variant) {
        Set<PerformanceTimer> perfTimer = getApplication().startPerformanceMonitoring(this.getClass().getSimpleName() + ":postForm");
        ResponseWrapper<T> handledRequest = doPost(form, variant);
        getApplication().stopPerformanceMonitoring(perfTimer);
        if (handledRequest.getConstraintViolationsResponse() != null) {
            return handledRequest.getConstraintViolationsResponse();
        }
        return new FormResponse<T>(getResponse(), handledRequest.getEntity(), ".");
    }

    private ResponseWrapper<T> doPost(Form form, Variant variant) {
        log.info("Request entry point: {} @Post('x-www-form-urlencoded:html|json|xml')", this.getClass()
                .getSimpleName());
        ClientInfo ci = getRequest().getClientInfo();
        log.info("calling post(Form), media types '{}'", ci != null ? ci.getAcceptedMediaTypes() : "test");
        if (form != null) {
            getRequest().getAttributes().put(SKYSAIL_SERVER_RESTLET_FORM, form);
        }
        if (variant != null) {
            getRequest().getAttributes().put(SKYSAIL_SERVER_RESTLET_VARIANT, variant);
        }
        RequestHandler<T> requestHandler = new RequestHandler<T>(getApplication());
        AbstractResourceFilter<PostEntityServerResource<T>, T> handler = requestHandler.createForPost();
        getResponse().setStatus(Status.SUCCESS_CREATED);
        return handler.handle(this, getResponse());
    }

    @Override
    public LinkRelation getLinkRelation() {
        return LinkRelation.CREATE_FORM;
    }

    protected Set<ConstraintViolation<T>> validate(T entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Link> getLinks() {
        return Arrays.asList(new Link.Builder(".").relation(LinkRelation.NEXT).title("form target").verbs(Method.POST)
                .build());
    }

    protected void index(T entity, SearchService searchService, String link, String id) {
        if (searchService == null) {
            log.warn("no search service available - document will not be indexed");
            return;
        }
        try {
            Map<String, String> getMap = describe(entity);
            getMap.put("_owner", SecurityUtils.getSubject().getPrincipal().toString());
            getMap.put("_link", link.replace("{id}", id));
            searchService.addDocument(getMap);
        } catch (IOException e) {
            log.error("error indexing document", e);
        }

    }

}
