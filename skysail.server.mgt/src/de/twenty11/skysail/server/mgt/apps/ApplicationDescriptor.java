package de.twenty11.skysail.server.mgt.apps;

import de.twenty11.skysail.server.app.ApplicationProvider;
import io.skysail.api.forms.Field;
import io.skysail.domain.Identifiable;
import io.skysail.domain.core.ApplicationModel;
import lombok.*;

@Setter
public class ApplicationDescriptor implements Identifiable {

    @Getter
    private String id;

    @Field
    @Getter
    private String name;

    @Getter
    private ApplicationModel applicationModel;

    public ApplicationDescriptor(ApplicationProvider provider) {
        applicationModel = provider.getApplication().getApplicationModel();
        name = provider.getApplication().getName();
        id = name;
    }

    public ApplicationDescriptor(String name) {
        this.name = name;
    }

}
