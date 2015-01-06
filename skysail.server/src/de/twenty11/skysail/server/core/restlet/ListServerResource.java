package de.twenty11.skysail.server.core.restlet;

/**
 *  Copyright 2011 Carsten GrGraeff
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.restlet.Application;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.ClientInfo;
import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Options;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import de.twenty11.skysail.api.responses.LinkHeaderRelation;
import de.twenty11.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.apidoc.API;
import de.twenty11.skysail.server.app.TranslationProvider;
import de.twenty11.skysail.server.core.restlet.filter.AbstractResourceFilter;
import etm.core.monitor.EtmPoint;

/**
 * A ListServerResource implementation takes care of a List of Entities.
 *
 * Example Usage:
 *
 * <pre>
 * <code>
 *    private MailApplication app;
 *    ...
 * 
 *    public AccountsResource() {
 *        app = (MailApplication) getApplication();
 *    }
 * 
 *     @Override
 *    protected void doInit() throws ResourceException {
 *        super.doInit();
 *        connectionName = (String) getRequest().getAttributes().get("conn");
 *    }
 * 
 *    @Override
 *    public List<Account> getData() {
 *        return app.getAccountRepository().getAll();
 *    }
 * 
 *    @Override
 *    public String getMessage(String key) {
 *        return "List Accounts";
 *    }
 * 
 *    @Override
 *    public List<Link> getLinks() {
 *        List<Link> links = super.getLinks();
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
 * @author carsten
 *
 */
@Slf4j
public abstract class ListServerResource<T> extends SkysailServerResource<List<T>> {

    private static final int FAVORITE_MAX_LENGTH_IN_GUI = 11;

    public static final String CONSTRAINT_VIOLATIONS = "constraintViolations";

    private String filterExpression;

    private Class<? extends EntityServerResource<T>> associatedEntityServerResource;
    
    /**
     * Default constructor without associatedEntityServerResource
     */
    public ListServerResource() {
    	addToContext(ResourceContextId.LINK_TITLE, "list");
    }

    /**
     * Constructor which associates this ListServerResource with a corresponding
     * EntityServerResource.
     * 
     * @param entityResourceClass
     */
    public ListServerResource(Class<? extends EntityServerResource<T>> entityResourceClass) {
    	this();
        this.associatedEntityServerResource = entityResourceClass;
    }

    @Get("html|json|csv|treeform")
    @API(desc = "lists the entities according to the media type provided")
    public List<T> getEntities() {
    	EtmPoint point = etmMonitor.createPoint("ListServerResource:getEntities");
        log.info("Request entry point: {} @Get('html|json|csv|treeform')", this.getClass().getSimpleName());
        ClientInfo ci = getRequest().getClientInfo();
        log.info("calling getEntities, media types '{}'", ci != null ? ci.getAcceptedMediaTypes() : "test");

        List<T> response = getEntities("default implementation... you might want to override ListServerResource2#getEntities in "
                + this.getClass().getName());
        // response.addLinks(getLinks());
        point.collect();
        return response;
    }

    @Options
    public void doOptions(Representation entity) {
    	EtmPoint point = etmMonitor.createPoint("ListServerResource:doOptions");
        Form responseHeaders = (Form) getResponse().getAttributes().get("org.restlet.http.headers");
        if (responseHeaders == null) {
            responseHeaders = new Form();
            getResponse().getAttributes().put("org.restlet.http.headers", responseHeaders);
        }
        if (AccessControlAllowOriginFeature.myIsActive()) {
            responseHeaders.add("Access-Control-Allow-Origin", "*");
            responseHeaders.add("Access-Control-Allow-Methods", "POST,OPTIONS");
            responseHeaders.add("Access-Control-Allow-Headers", "Content-Type");
            responseHeaders.add("Access-Control-Allow-Credentials", "false");
            responseHeaders.add("Access-Control-Max-Age", "60");
        }
        point.collect();
    }

    /**
     * if (getRequest().getAttributes().get("id") != null) { folderId = (String)
     * getRequest().getAttributes().get("id"); }
     */
    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        filterExpression = getQuery() != null ? getQuery().getFirstValue("filter") : "";
    }

    @Override
    public LinkHeaderRelation getLinkRelation() {
        return LinkHeaderRelation.COLLECTION;
    }

    public String getLinkName(Object... substitutions) {
        Application application = getApplication();
        if (application instanceof TranslationProvider) {
            String linkKey = getClass().getName() + ".link";
            String translation = ((TranslationProvider) application).translate(linkKey, linkKey, this, false,
                    substitutions);
            return translation.length() > FAVORITE_MAX_LENGTH_IN_GUI ? translation.substring(0,
                    FAVORITE_MAX_LENGTH_IN_GUI - 3) + "..." : translation;
        }
        return "list";
    }

    protected List<T> getEntities(String defaultMsg) {
        RequestHandler<T> requestHandler = new RequestHandler<T>(null);
        AbstractResourceFilter<ListServerResource<T>, List<T>> chain = requestHandler.createForList(Method.GET, null);
        return chain.handle(this, getResponse()).getEntity();
    }

    /**
     * We have cases where we can retrieve JSON representations "early", for example when using a noSQL database. 
     * In this case, we don't want to create objects of type T and then let them converted back to JSON by the
     * JacksonConverter.
     */
    protected List<String> getEntitiesAsJson() {
        RequestHandler<String> requestHandler = new RequestHandler<String>(null);
        AbstractResourceFilter<ListServerResource<String>, List<String>> chain = requestHandler.createForList(Method.GET, null);
        ListServerResource<String> resource = new ListServerResource<String>() {
            @Override
            public List<String> getData() {
            	return ListServerResource.this.getDataAsJson();
            }

            @Override
            public Request getRequest() {
                return ListServerResource.this.getRequest();
            }

            @Override
            public Response getResponse() {
                return ListServerResource.this.getResponse();
            }
        };
        return chain.handle(resource, getResponse()).getEntity();
    }

    protected List<String> getDataAsJson() {
	    return Arrays.asList("overwrite in subclass");
    }

	/**
     * will be called in case of a DELETE request. Override in subclasses if
     * they support DELETE requests.
     */
    public SkysailResponse<?> eraseEntity() {
        throw new UnsupportedOperationException();
    }

    public Class<? extends EntityServerResource<T>> getAssociatedEntityResource() {
        return associatedEntityServerResource;
    }

    protected Map<String, String> getParamsFromRequest() {
        Map<String, String> params = new HashMap<String, String>();
        if (getQuery() != null) {
            params = getQuery().getValuesMap();
        }
        return params;
    }

    protected String augmentWithFilterMsg(String msg) {
        return filterExpression == null ? msg : msg + " filtered by '" + filterExpression + "'";
    }

    protected boolean filterMatches(T t) {
        if (filterExpression != null && filterExpression.trim().length() != 0) {
            return match(t, filterExpression);
        } else {
            return true;
        }
    }

    protected boolean match(T t, String pattern) {
        return true;
    }


}
