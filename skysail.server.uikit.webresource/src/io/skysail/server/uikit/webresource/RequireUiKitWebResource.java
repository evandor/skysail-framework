package io.skysail.server.uikit.webresource;

import java.lang.annotation.*;

import aQute.bnd.annotation.headers.RequireCapability;
import osgi.enroute.namespace.WebResourceNamespace;

@RequireCapability(ns = WebResourceNamespace.NS, filter = "(&(" + WebResourceNamespace.NS
        + "="+UiKitConstants.BOOTSTRAP_WEB_RESOURCE_NAME+")${frange;"+UiKitConstants.BOOTSTRAP_WEB_RESOURCE_VERSION+"})")
@Retention(RetentionPolicy.CLASS)
public @interface RequireUiKitWebResource {

    /**
     * Define the default resource to return
     * 
     * @return the list of resources to include
     */
    String[] resource() default "bootstrap.css";

    /**
     * Define the priority of this web resources. The higher the priority, the
     * earlier it is loaded when all web resources are combined.
     * 
     * @return the priority
     */
    int priority() default 1000;
}
