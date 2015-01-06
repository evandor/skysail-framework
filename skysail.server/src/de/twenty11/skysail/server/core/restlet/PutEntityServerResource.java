package de.twenty11.skysail.server.core.restlet;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintValidatorFactory;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.api.responses.FormResponse;
import de.twenty11.skysail.api.responses.LinkHeaderRelation;
import de.twenty11.skysail.api.responses.Linkheader;
import de.twenty11.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.filter.AbstractResourceFilter;
import etm.core.monitor.EtmPoint;

/**
 * An abstract resource template dealing with PUT requests (see
 * http://www.ietf.org/rfc/rfc2616.txt, 9.6).
 * 
 * Process:
 * 
 * todo
 * 
 * Example implementing class:
 * 
 * <pre>
 *  <code>
 *  public class MyEntityResource extends PutEntityServerResource&lt;MyEntity&gt; {
 * 
 *     private MyApplication app;
 *     private String myEntityId;
 * 
 *     public MyEntityResource() {
 *        app = (MyApplication) getApplication();
 *     }
 * 
 *     protected void doInit() throws ResourceException {
 *       id = getAttribute("id");
 *     }
 *     
 *     public T getEntity() {
 *        return app.getRepository().getById(myEntityId)
 *     }
 * }
 * </code>
 * </pre>
 * 
 * @param <T>
 */
public abstract class PutEntityServerResource<T> extends SkysailServerResource<T> {

    private static final String SKYSAIL_SERVER_RESTLET_FORM = "de.twenty11.skysail.server.core.restlet.form";
    private static final String SKYSAIL_SERVER_RESTLET_ENTITY = "de.twenty11.skysail.server.core.restlet.entity";

    private static final Logger logger = LoggerFactory.getLogger(PutEntityServerResource.class);

    public PutEntityServerResource() {
        addToContext(ResourceContextId.LINK_TITLE, "update");
    }

    /**
     * If you have a route defined as "/repository/{key}", you can get the key
     * like this: key = (String) getRequest().getAttributes().get("key");
     * 
     * To get hold on any parameters passed, consider using this pattern:
     * 
     * Form form = new Form(getRequest().getEntity()); action =
     * form.getFirstValue("action");
     * 
     */
    @Override
    protected void doInit() throws ResourceException {
        // empty
    };

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
    public abstract T getEntity();

    /**
     * will be called in case of a PUT request.
     */
    public abstract SkysailResponse<?> updateEntity(T entity);

    @Override
    public T getData() {
        return getEntity();
    }

    /**
     * This method will be called by the skysail framework to create the actual
     * resource from its form representation.
     * 
     * @param form
     *            the representation of the resource as a form
     * @return the resource of type T
     * @throws ParseException
     */
    public T getData(Form form) {
        return populate(getEntity(), form);
    }

    @Get("htmlform|html")
    public SkysailResponse<T> createForm() {
        logger.info("Request entry point: {} @Get('htmlform|html')", this.getClass().getSimpleName());
        return new FormResponse<T>(getEntity(), getAttribute("id"), ".", redirectBackTo());
    }

    protected String redirectBackTo() {
        return null;
    }

    @Get("json")
    public T getJson() {
        logger.info("Request entry point: {} @Get('json')", this.getClass().getSimpleName());
        RequestHandler<T> requestHandler = new RequestHandler<T>(getApplication());
        // AbstractResourceFilter<PutEntityServerResource<T>, T> handler =
        // requestHandler.newInstance(Method.GET);
        return null;// handler.handle(this, getResponse()).getEntity();
    }

    @Put("json")
    public Object putEntity(T entity) {
        EtmPoint point = etmMonitor.createPoint("PutEntityServerResource:putEntity");
        logger.info("Request entry point: {} @Put('json')", this.getClass().getSimpleName());
        getRequest().getAttributes().put(SKYSAIL_SERVER_RESTLET_ENTITY, entity);
        T put = put((Form) null);
        point.collect();
        return put;
    }

    @Put("x-www-form-urlencoded:html|json")
    public T put(Form form) {
        EtmPoint point = etmMonitor.createPoint("PutEntityServerResource:put");
        logger.info("Request entry point: {} @Put('x-www-form-urlencoded:html|json')", this.getClass().getSimpleName());
        if (form != null) {
            getRequest().getAttributes().put(SKYSAIL_SERVER_RESTLET_FORM, form);
        }
        RequestHandler<T> requestHandler = new RequestHandler<T>(getApplication());
        AbstractResourceFilter<PutEntityServerResource<T>, T> handler = requestHandler
                .createForPut(getConstraintValidatorFactory());
        T entity = handler.handle(this, getResponse()).getEntity();
        point.collect();
        return entity;
    }

    @Override
    public LinkHeaderRelation getLinkRelation() {
        return LinkHeaderRelation.CREATE_FORM;
    }

    @Override
    public String getLinkName() {
        return "edit";
    }

    @Override
    public String getImageRef() {
        return "<span class='glyphicon glyphicon-edit' aria-hidden='true'></span>";
    }

    protected ConstraintValidatorFactory getConstraintValidatorFactory() {
        return null;
    }

    public Validator getValidator() {
        return null;
    }

    protected Set<ConstraintViolation<T>> validate(@SuppressWarnings("unused") T entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Linkheader> getLinkheader() {
        return Arrays.asList(new Linkheader.Builder(".").relation(LinkHeaderRelation.NEXT).title("form target")
                .verbs(Method.POST).build());
    }

}
