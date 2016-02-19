package io.skysail.server.w2ui.webresource;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.event.EventAdmin;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.CacheDirective;
import org.restlet.data.LocalReference;
import org.restlet.data.Status;
import org.restlet.routing.Filter;
import org.restlet.routing.Router;

import de.twenty11.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.services.ThemeDefinition;
import io.skysail.server.services.ThemeProvider;
import io.skysail.server.utils.ClassLoaderDirectory;
import io.skysail.server.utils.CompositeClassLoader;
import lombok.Getter;

@org.osgi.service.component.annotations.Component
public class WebappApplication extends SkysailApplication implements ApplicationProvider, ThemeProvider {

    private static final String ROOT = "static"+W2UiConstants.W2UI_WEB_RESOURCE_NAME;
    
    @org.osgi.service.component.annotations.Reference(cardinality = ReferenceCardinality.OPTIONAL)
    @Getter
    private volatile EventAdmin eventAdmin;
    
    public WebappApplication() {
        this("webapp serving static files");
    }

    public WebappApplication(String staticPathTemplate) {
        super(ROOT);
        setName(ROOT);
    }

    
    @Override
    public Restlet createInboundRoot() {

        LocalReference localReference = LocalReference.createClapReference(LocalReference.CLAP_THREAD, "/"+ROOT+"/");

        CompositeClassLoader customCL = new CompositeClassLoader();
        customCL.addClassLoader(Thread.currentThread().getContextClassLoader());
        customCL.addClassLoader(Router.class.getClassLoader());
        customCL.addClassLoader(this.getClass().getClassLoader());

        ClassLoaderDirectory staticDirectory = new ClassLoaderDirectory(getContext(), localReference, customCL);

        Filter cachingFilter = new Filter() {
            @Override
            public Restlet getNext() {
                return staticDirectory;
            }

            @Override
            protected void afterHandle(Request request, Response response) {
                super.afterHandle(request, response);
                if (response.getEntity() != null) {
                    if (request.getResourceRef().toString(false, false).contains("nocache")) {
                        response.getEntity().setModificationDate(null);
                        response.getEntity().setExpirationDate(null);
                        response.getEntity().setTag(null);
                        response.getCacheDirectives().add(CacheDirective.noCache());
                    } else {
                        response.setStatus(Status.SUCCESS_OK);
                        Calendar c = new GregorianCalendar();
                        c.setTime(new Date());
                        c.add(Calendar.DAY_OF_MONTH, 10);
                        response.getEntity().setExpirationDate(c.getTime());
                        response.getEntity().setModificationDate(null);
                        response.getCacheDirectives().add(CacheDirective.publicInfo());
                    }
                }
            }
        };

        Router router = new Router(getContext());
        router.attachDefault(cachingFilter);
        return router;
    }

    @Override
    public SkysailApplication getApplication() {
        return this;
    }

    @Override
    public ThemeDefinition getTheme() {
        return new ThemeDefinition(W2UiConstants.W2UI_WEB_RESOURCE_SHORTNAME, W2UiConstants.W2UI_WEB_RESOURCE_VERSION, "desc");
    }

}
