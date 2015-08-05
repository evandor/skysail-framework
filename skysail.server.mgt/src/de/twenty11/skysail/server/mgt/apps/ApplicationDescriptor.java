package de.twenty11.skysail.server.mgt.apps;

import io.skysail.server.app.SkysailApplication;

import org.restlet.Application;

public class ApplicationDescriptor {

    private String name;

    public ApplicationDescriptor(Application app) {
        if (app instanceof SkysailApplication) {
            SkysailApplication skysailApp = (SkysailApplication) app;
            name = app.getName();
        }
    }

    public String getName() {
        return name;
    }

}
