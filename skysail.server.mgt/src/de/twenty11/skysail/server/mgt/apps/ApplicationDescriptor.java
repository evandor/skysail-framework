package de.twenty11.skysail.server.mgt.apps;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.Field;
import io.skysail.server.domain.core.Application;
import lombok.*;
import de.twenty11.skysail.server.app.ApplicationProvider;

@Setter
public class ApplicationDescriptor implements Identifiable {

    @Getter
    private String id;

    @Field
    @Getter
    private String name;

    @Getter
    private Application applicationModel;

    public ApplicationDescriptor(ApplicationProvider provider) {
        applicationModel = provider.getApplication().getApplicationModel();
        name = provider.getApplication().getName();
        id = name;
    }

    public ApplicationDescriptor(String name) {
        this.name = name;
    }

}
