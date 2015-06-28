package io.skysail.server.polymer;

import io.skysail.server.app.SkysailApplication;
import io.skysail.server.utils.*;

import java.util.*;

import org.restlet.*;
import org.restlet.data.*;
import org.restlet.routing.*;

import aQute.bnd.annotation.component.Component;
import de.twenty11.skysail.server.app.ApplicationProvider;

@Component
public class PolymerStaticWebapp extends SkysailApplication implements ApplicationProvider {

    public PolymerStaticWebapp() {
        this("webapp serving polymer files");
    }

    public PolymerStaticWebapp(String staticPathTemplate) {
        super("_polymer");
        setName("_polymer");
    }

    @Override
    public Restlet createInboundRoot() {

        LocalReference localReference = LocalReference.createClapReference(LocalReference.CLAP_THREAD, "/_polymer/");

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

}
