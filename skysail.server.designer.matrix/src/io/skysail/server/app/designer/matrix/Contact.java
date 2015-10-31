package io.skysail.server.app.designer.matrix;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.Field;
import io.skysail.server.ext.apt.annotations.GenerateResources;

import javax.persistence.Id;

import lombok.*;

@Getter
@Setter
@GenerateResources(application = "io.skysail.server.app.designer.matrix.MatrixApplication")
public class Contact implements Identifiable {

    @Id
    private String id;

    @Field
    private String title, firstName, lastName, pic, location, room, department, telephone, account, description;
}
