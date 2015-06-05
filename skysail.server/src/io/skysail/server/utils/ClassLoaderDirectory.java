package io.skysail.server.utils;

import org.restlet.*;
import org.restlet.data.Reference;
import org.restlet.resource.Directory;

/**
 * http://maxrohde.com/2010/09/29/clap-protocol-in-restlet-and-osgi/
 * 
 */
public class ClassLoaderDirectory extends Directory {

    private ClassLoader cl;

    public ClassLoaderDirectory(Context context, Reference rootLocalReference, ClassLoader cl) {
        super(context, rootLocalReference);
        this.cl = cl;
    }

    @Override
    public void handle(Request request, Response response) {
        final ClassLoader saveCL = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(cl);
        super.handle(request, response);
        Thread.currentThread().setContextClassLoader(saveCL);
    }
}
