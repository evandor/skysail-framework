package io.skysail.server.restlet.resources;

import java.util.*;

import org.restlet.Restlet;
import org.restlet.data.Method;
import org.restlet.representation.Variant;
import org.restlet.resource.*;

import io.skysail.api.links.LinkRelation;
import io.skysail.api.responses.*;
import io.skysail.domain.Identifiable;
import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.ListRequestHandler;
import io.skysail.server.restlet.response.ListResponseWrapper;
import io.skysail.server.services.PerformanceTimer;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

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
 *    public List&lt;Company&gt; getEntity() {
 *      return app.getRepository().find(new Filter(getRequest()));
 *    }
 *
 *    {@literal @}Override
 *    public List&lt;Link&gt; getLinks() {
 *       return super.getLinks(PostCompanyResource.class);
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
@ToString
public abstract class ListServerResource<T extends Identifiable> extends SkysailServerResource<List<?>> {

    public static final String CONSTRAINT_VIOLATIONS = "constraintViolations";

    private List<Class<? extends SkysailServerResource<?>>> associatedEntityServerResources;

    private ListRequestHandler<?> requestHandler;

    /**
     * Default constructor without associatedEntityServerResource.
     */
    public ListServerResource() {
        requestHandler = new ListRequestHandler<T>(null);
        addToContext(ResourceContextId.LINK_TITLE, "list");
    }
    
    /**
     * Constructor which associates this ListServerResource with a corresponding
     * EntityServerResource.
     *
     * @param skysailServerResource
     *            the class
     */
    @SafeVarargs
    public ListServerResource(Class<? extends SkysailServerResource<?>>... skysailServerResource) {
        this();
        if (skysailServerResource != null) {
            this.associatedEntityServerResources = Arrays.asList(skysailServerResource);
        }
    }

    /**
     * returns the list of entities in the case of a GET request with media
     * types html, json etc.
     *
     * @return the list of entities in html, csv or treeform format
     */
    @Get("html|json|yaml|xml|csv|timeline|standalone")
    // treeform, csv:broken http://stackoverflow.com/questions/24569318/writing-multi-line-csv-with-jacksonrepresentation
    // https://github.com/restlet/restlet-framework-java/issues/928
    public ListServerResponse<T> getEntities(Variant variant) {
        Set<PerformanceTimer> perfTimer = getApplication().startPerformanceMonitoring(
                this.getClass().getSimpleName() + ":getEntities");
        log.info("Request entry point: {} @Get({})", this.getClass().getSimpleName(),
                variant);
        List<T> response = listEntities();
        getApplication().stopPerformanceMonitoring(perfTimer);
        return new ListServerResponse<>(getResponse(), response);

        // if (SecurityFeatures.ALLOW_ORIGIN_FEATURE.isActive()) {
        // responseHeaders.add("Access-Control-Allow-Origin", "*");
        // responseHeaders.add("Access-Control-Allow-Methods",
        // "GET,POST,OPTIONS");
        // responseHeaders.add("Access-Control-Allow-Headers", "Content-Type");
        // responseHeaders.add("Access-Control-Allow-Credentials", "false");
        // responseHeaders.add("Access-Control-Max-Age", "60");
        // }
    }

    @Override
    public LinkRelation getLinkRelation() {
        return LinkRelation.COLLECTION;
    }

    @SuppressWarnings("unchecked")
    private final List<T> listEntities() {
        ListResponseWrapper<?> responseWrapper = requestHandler.createForList(Method.GET).handleList(this, getResponse());
        return (List<T>) responseWrapper.getEntity();
    }

    /**
     * will be called in case of a DELETE request. Override in subclasses if
     * they support DELETE requests.
     *
     * @return the response
     */
    public SkysailResponse<T> eraseEntity() {
        throw new UnsupportedOperationException();
    }

    public List<Class<? extends SkysailServerResource<?>>> getAssociatedServerResources() {
        return associatedEntityServerResources;
    }

}
