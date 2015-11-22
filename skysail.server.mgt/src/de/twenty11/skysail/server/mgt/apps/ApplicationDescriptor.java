package de.twenty11.skysail.server.mgt.apps;

import io.skysail.api.domain.Identifiable;
import io.skysail.server.app.SkysailApplication;
import lombok.*;

import org.restlet.Application;

@Getter
@Setter
public class ApplicationDescriptor implements Identifiable {

    private String id;

    private String name;

    public ApplicationDescriptor(Application app) {
        if (app instanceof SkysailApplication) {
            SkysailApplication skysailApp = (SkysailApplication) app;
            name = app.getName();
        }
    }

}
