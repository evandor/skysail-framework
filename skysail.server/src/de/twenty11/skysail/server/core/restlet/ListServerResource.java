package de.twenty11.skysail.server.core.restlet;

import io.skysail.api.documentation.API;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.Header;
import org.restlet.data.Method;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.Get;
import org.restlet.resource.Options;
import org.restlet.resource.ServerResource;
import org.restlet.util.Series;

import de.twenty11.skysail.api.responses.LinkHeaderRelation;
import de.twenty11.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.filter.AbstractResourceFilter;
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
 *    protected void doInit() throws ResourceException {
 *        app = (MailApplication) getApplication();
 *        connectionName = (String) getRequest().getAttributes().get("conn");
 *    }
 * 
 *    {@literal @}Override
 *    public List&lt;Account&gt; getData() {
 *        return app.getAccountRepository().getAll();
 *    }
 * 
 *    {@literal @}Override
 *    public String getMessage(String key) {
 *        return "List Accounts";
 *    }
 * 
 *    {@literal @}Override
 *    public List&lt;Link&gt; getLinks() {
 *        List&lt;Link&gt; links = super.getLinks();
 *        links.add(new RelativeLink(getContext(), "mail/accounts/?media=htmlform", "new Account"));
 *        return links;
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
     * types html, csv or treeform.
     * 
     * @return the list of entities in html, csv or treeform format
     */
    @Get("html|csv|treeform")
    @API(desc = "lists the entities according to the media type provided")
    public final List<T> getEntities(Variant variant) {
        EtmPoint point = etmMonitor.createPoint("ListServerResource:getEntities");
        log.info("Request entry point: {} @Get('html|csv|treeform') with variant {}", this.getClass().getSimpleName(),
                variant);
        List<T> response = listEntities();
        point.collect();
        return response;
    }

    /**
     * returns the list of entities in the case of a GET request with media
     * types JSON.
     * 
     * @return the list of entities in JSON format
     */
    @Get("json")
    @API(desc = "lists the entities in JSON format")
    public final List<String> getAsJson(Variant variant) {
        EtmPoint point = etmMonitor.createPoint("ListServerResource:getAsJson");
        log.info("Request entry point: {} @Get('html|json') with variant {}", this.getClass().getSimpleName(), variant);
        List<String> response = getDataAsJson();
        Series<Header> responseHeaders = getResponse().getHeaders();
        // if (SecurityFeatures.ALLOW_ORIGIN_FEATURE.isActive()) {
        responseHeaders.add("Access-Control-Allow-Origin", "*");
        responseHeaders.add("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
        responseHeaders.add("Access-Control-Allow-Headers", "Content-Type");
        responseHeaders.add("Access-Control-Allow-Credentials", "false");
        responseHeaders.add("Access-Control-Max-Age", "60");
        // }
        point.collect();
        return response;
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
    public LinkHeaderRelation getLinkRelation() {
        return LinkHeaderRelation.COLLECTION;
    }

    private final List<T> listEntities() {
        ResponseWrapper<List<T>> responseWrapper = requestHandler.createForList(Method.GET).handle(this, getResponse());
        return responseWrapper.getEntity();
    }

    /**
     * We have cases where we can retrieve JSON representations "early", for
     * example when using a noSQL database. In this case, we don't want to
     * create objects of type T and then let them converted back to JSON by the
     * JacksonConverter.
     *
     * @return the result
     */
    protected final List<String> listEntitiesAsJson() {
        AbstractResourceFilter<ListServerResource<String>, List<String>> chain = stringRequestHandler
                .createForList(Method.GET);
        ListServerResource<String> resource = new ListServerResource<String>() {
            // @Override
            // public List<String> getData() {
            // return ListServerResource.this.getDataAsJson();
            // }

            @Override
            public Request getRequest() {
                return ListServerResource.this.getRequest();
            }

            @Override
            public Response getResponse() {
                return ListServerResource.this.getResponse();
            }
        };
        List<String> entity = chain.handle(resource, getResponse()).getEntity();
        if (entity == null) {
            entity = Collections.emptyList();
        }
        return entity;
    }

    @Override
    public List<T> getEntity() {
        return Collections.emptyList();// throw new
        // IllegalStateException("deprectead API");
    }

    protected List<String> getDataAsJson() {
        return Arrays.asList("overwrite " + ListServerResource.class.getSimpleName() + "#getDataAsJson in subclass");
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
