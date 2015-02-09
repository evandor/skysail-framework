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

import org.restlet.Restlet;
import org.restlet.data.LocalReference;
import org.restlet.routing.Router;

import aQute.bnd.annotation.component.Component;
import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.app.SkysailApplication;
import de.twenty11.skysail.server.utils.ClassLoaderDirectory;
import de.twenty11.skysail.server.utils.CompositeClassLoader;

@Component
public class WebappApplication extends SkysailApplication implements ApplicationProvider {

    public WebappApplication() {
        this("webapp serving static files");
    }

    /**
     * @param staticPathTemplate
     * @param bundleContext
     */
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

        Router router = new Router(getContext());
        router.attachDefault(staticDirectory);
        return router;
    }

    @Override
    public SkysailApplication getApplication() {
        return this;
    }

}
