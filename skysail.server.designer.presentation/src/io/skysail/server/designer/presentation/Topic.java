package io.skysail.server.designer.presentation;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.Field;
import io.skysail.server.codegen.annotations.GenerateResources;

import javax.persistence.Id;

import lombok.*;

@Getter
@Setter
@GenerateResources(application = "io.skysail.server.designer.presentation.InteractivePresentationApplication")
public class Topic implements Identifiable {

    @Id
    private String id;

    @Field
    private String topic;


}
