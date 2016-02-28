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

package de.twenty11.skysail.server.osgi;

import java.util.*;

import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.*;
import org.osgi.service.event.EventAdmin;

import de.twenty11.skysail.server.core.restlet.*;
import de.twenty11.skysail.server.osgi.osgimonitor.resources.*;
import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.menus.*;
import lombok.Getter;

@Component
public class OsgiMonitorViewerApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    private ConfigurationAdmin configadmin;
    
    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    @Getter
    private volatile EventAdmin eventAdmin;

    public OsgiMonitorViewerApplication() {
        super("OSGi");
        setDescription("RESTful OsgiMonitor bundle");
        setOwner("twentyeleven");
        setName("OSGi");
        setSecuredByRoles("admin");
        addToAppContext(ApplicationContextId.IMG, "/static/img/silk/server_connect.png");
    }

    @Override
    protected void attach() {
        super.attach();
        router.attach(new RouteBuilder("", OsgiMonitorRootResource.class));
        router.attach(new RouteBuilder("/", OsgiMonitorRootResource.class));
        router.attach(new RouteBuilder("/bundles", BundlesResource.class).authorizeWith(anyOf("admin")));

        router.attach(new RouteBuilder("/bundlesgraph", BundlesGraphResource.class).authorizeWith(anyOf("admin")));

     //   router.attach(new RouteBuilder("/bundles/asGraph", IFrameResource.class).setText("Bundles as visualized Graph"));
        //router.attach(new RouteBuilder("/bundles/asGraph/", BundlesAsGraphResource.class).setText("Json Graph representation"));
     //   router.attach(new RouteBuilder("/bundles/asGraph/d3Simple", BundlesAsD3GraphResource.class));
        router.attach(new RouteBuilder("/bundles/details/{bundleId}", BundleResource.class));
        router.attach(new RouteBuilder("/bundles/details/{bundleId}/action", BundleResource.class));
//        router.attach(new RouteBuilder("/bundles/details/{bundleId}/headers", HeaderResource.class));
        router.attach(new RouteBuilder("/services", ServicesResource.class).setText("Services"));
        router.attach(new RouteBuilder("/services/{serviceId}", ServiceResource.class));
//        router.attach(new RouteBuilder("/capabilities", CapabilitiesResource.class).setText("Capabilities"));
        router.attach(new RouteBuilder("/requirements", RequirementsResource.class).setText("Requirements"));

//        router.attach(new RouteBuilder("/configadmin", ConfigAdminResource.class).setText("ConfigAdmin"));
       // router.attach(new RouteBuilder("/permissionadmin", PermissionAdminResource.class).setText("PermissionAdmin"));

       // router.attach(new RouteBuilder("/remotebundles", RemoteBundlesResource.class).setText("RemoteBundles"));
//        router.attach(new RouteBuilder("/graph", BundlesGraphResource.class));
//        router.attach(new RouteBuilder("/bundles/graph", BGResource.class));
        // @formatter:on
    }

    public synchronized void setConfigAdmin(ConfigurationAdmin configadmin) {
        this.configadmin = configadmin;
    }

    public ConfigurationAdmin getConfigadmin() {
        return configadmin;
    }
    
    public List<MenuItem> getMenuEntries() {
        MenuItem appMenu = new MenuItem(getName(), "/" + getName() + getApiVersion().getVersionPath());
        appMenu.setCategory(MenuItem.Category.ADMIN_MENU);
        return Arrays.asList(appMenu);
    }


}
