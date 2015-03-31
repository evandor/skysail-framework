package de.twenty11.skysail.server.core.restlet;

import io.skysail.api.documentation.API;
import io.skysail.api.links.LinkRelation;
import io.skysail.api.responses.SkysailResponse;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.restlet.Restlet;
import org.restlet.data.Header;
import org.restlet.data.Method;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.Get;
import org.restlet.resource.Options;
import org.restlet.resource.ServerResource;
import org.restlet.util.Series;

import etm.core.monitor.EtmPoint;

/**
 * A ListServerResource implementation takes care of a List of Entities.
 * 
 * <p>
 * Typically, the request issuer provides headers defining the accepted media
 * types. Depending on those headers this implementation will provide the
 * entities associated with the current URI in various formats such as JSON,
 * Html, etc.
 * </p>
 * 
 * <p>
 * In a browser, you can add something like <code>?media=json</code> to the URL
 * to get the desired representation
 * </p>
 *
 * <p>
 * Concrete subclass example:
 * </p>
 *
 * <pre>
 * <code>
 *    private MailApplication app;
 *    ...
 * 
 *    {@literal @}Override
 *    protected void doInit() {
 *        app = (MailApplication) getApplication();
 *    }
 * 
 *    {@literal @}Override
 *    public List<Company> getEntity() {
 *       return app.getRepository().findAll(Company.class);
 *    }
 *     
 *    {@literal @}Override
 *    public List<Link> getLinkheader() {
 *       return super.getLinkheader(PostCompanyResource.class);
 *    } 
 * </code>
 * </pre>
 *
 * <br>
 * Concurrency note from parent: contrary to the {@link org.restlet.Uniform}
 * class and its main {@link Restlet} subclass where a single instance can
 * handle several calls concurrently, one instance of {@link ServerResource} is
 * created for each call handled and accessed by only one thread at a time.
 *
 */
@Slf4j
public abstract class ListServerResource<T> extends SkysailServerResource<List<T>> {

    public static final String CONSTRAINT_VIOLATIONS = "constraintViolations";

    private Class<? extends EntityServerResource<T>> associatedEntityServerResource;
    private RequestHandler<T> requestHandler;
    private RequestHandler<String> stringRequestHandler;

    /**
     * Default constructor without associatedEntityServerResource.
     */
    public ListServerResource() {
        requestHandler = new RequestHandler<T>(null);
        stringRequestHandler = new RequestHandler<String>(null);
        addToContext(ResourceContextId.LINK_TITLE, "list");
    }

    /**
     * Constructor which associates this ListServerResource with a corresponding
     * EntityServerResource.
     * 
     * @param entityResourceClass
     *            the class
     */
    public ListServerResource(Class<? extends EntityServerResource<T>> entityResourceClass) {
        this();
        this.associatedEntityServerResource = entityResourceClass;
    }

    /**
     * returns the list of entities in the case of a GET request with media
     * types html, json etc.
     * 
     * @return the list of entities in html, csv or treeform format
     */
    @Get("html|json|yaml|xml")
    // treeform, csv:broken
    @API(desc = "lists the entities according to the media type provided")
    public final List<T> getEntities(Variant variant) {
        EtmPoint point = etmMonitor.createPoint("ListServerResource:getEntities");
        log.info("Request entry point: {} @Get('html|json|yaml|xml') with variant {}", this.getClass().getSimpleName(),
                variant);
        List<T> response = listEntities();
        point.collect();
        return response;

        // if (SecurityFeatures.ALLOW_ORIGIN_FEATURE.isActive()) {
        // responseHeaders.add("Access-Control-Allow-Origin", "*");
        // responseHeaders.add("Access-Control-Allow-Methods",
        // "GET,POST,OPTIONS");
        // responseHeaders.add("Access-Control-Allow-Headers", "Content-Type");
        // responseHeaders.add("Access-Control-Allow-Credentials", "false");
        // responseHeaders.add("Access-Control-Max-Age", "60");
        // }
    }

    /**
     * todo
     */
    @Options
    public final void doOptions(Representation entity) {
        EtmPoint point = etmMonitor.createPoint("ListServerResource:doOptions");
        Series<Header> responseHeaders = getResponse().getHeaders();
        // if (SecurityFeatures.ALLOW_ORIGIN_FEATURE.isActive()) {
        responseHeaders.add("Access-Control-Allow-Origin", "*");
        responseHeaders.add("Access-Control-Allow-Methods", "POST,GET,OPTIONS");
        responseHeaders.add("Access-Control-Allow-Headers", "Content-Type");
        responseHeaders.add("Access-Control-Allow-Credentials", "false");
        responseHeaders.add("Access-Control-Max-Age", "60");
        // }
        point.collect();
    }

    @Override
    public LinkRelation getLinkRelation() {
        return LinkRelation.COLLECTION;
    }

    private final List<T> listEntities() {
        ResponseWrapper<List<T>> responseWrapper = requestHandler.createForList(Method.GET).handle(this, getResponse());
        return responseWrapper.getEntity();
    }

    /**
     * will be called in case of a DELETE request. Override in subclasses if
     * they support DELETE requests.
     *
     * @return the response
     */
    public SkysailResponse<?> eraseEntity() {
        throw new UnsupportedOperationException();
    }

    public Class<? extends EntityServerResource<T>> getAssociatedEntityResource() {
        return associatedEntityServerResource;
    }

}
