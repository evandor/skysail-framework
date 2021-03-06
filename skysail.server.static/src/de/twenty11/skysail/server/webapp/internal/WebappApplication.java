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

package de.twenty11.skysail.server.webapp.internal;

import org.osgi.framework.FrameworkUtil;
import org.osgi.service.component.annotations.*;
import org.osgi.service.event.EventAdmin;
import org.restlet.Restlet;
import org.restlet.data.LocalReference;
import org.restlet.routing.Router;

import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.utils.*;
import lombok.Getter;

/**
 * @author carsten
 *
 */
@Component
public class WebappApplication extends SkysailApplication implements ApplicationProvider {

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    @Getter
    private volatile EventAdmin eventAdmin;

    // non-arg constructor needed for scr
    public WebappApplication() {
        this("dummy");
    }

    public WebappApplication(String staticPathTemplate) {
        super("static");
        setDescription("Static webapp bundle");
        setOwner("twentyeleven");
        setName("static");
    }

    @Override
    public Restlet createInboundRoot() {

        LocalReference localReference = LocalReference.createClapReference(LocalReference.CLAP_THREAD, "/static/");

        CompositeClassLoader customCL = new CompositeClassLoader();
        customCL.addClassLoader(Thread.currentThread().getContextClassLoader());
        customCL.addClassLoader(Router.class.getClassLoader());
        customCL.addClassLoader(this.getClass().getClassLoader());

        ClassLoaderDirectory staticDirectory = new ClassLoaderDirectory(getContext(), localReference, customCL);

        Router router = new Router(getContext());
        router.attachDefault(staticDirectory);
        return router;
    }

    @Override
    public SkysailApplication getApplication() {
        return this;
    }

    // TODO proper place for this here? what about multiple instances?
    protected void attach() {
        if (FrameworkUtil.getBundle(SkysailApplication.class) != null) {
            // urlMappingServiceListener = new UrlMappingServiceListener(this);
            // new SkysailApplicationServiceListener(this);
        }
    }

}
