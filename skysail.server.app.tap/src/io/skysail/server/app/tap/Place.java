package io.skysail.server.app.tap;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.Field;
import io.skysail.server.ext.apt.annotations.GenerateResources;

import javax.persistence.Id;

import lombok.*;

@Getter
@Setter
@GenerateResources(application = "io.skysail.server.app.tap.TabApplication")
public class Place implements Identifiable {

    @Id
    private String id;

    @Field
    private String name;

}
