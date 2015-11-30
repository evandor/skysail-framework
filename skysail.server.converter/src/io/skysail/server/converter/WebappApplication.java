/**
 *  Copyright 2011 Carsten Graef
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

package io.skysail.server.converter;

import java.util.*;

import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.event.EventAdmin;
import org.restlet.*;
import org.restlet.data.*;
import org.restlet.routing.*;

import de.twenty11.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.utils.*;
import lombok.Getter;

@org.osgi.service.component.annotations.Component
public class WebappApplication extends SkysailApplication implements ApplicationProvider { // NO_UCD

    @org.osgi.service.component.annotations.Reference(cardinality = ReferenceCardinality.OPTIONAL)
    @Getter
    private volatile EventAdmin eventAdmin;
    
    public WebappApplication() {
        this("webapp serving static files");
    }

    public WebappApplication(String staticPathTemplate) {
        super("webapp");
        setName("webapp");
    }

    
    @Override
    public Restlet createInboundRoot() {

        LocalReference localReference = LocalReference.createClapReference(LocalReference.CLAP_THREAD, "/webapp/");

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
