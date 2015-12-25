package io.skysail.server.domain.jvm.test;

import java.util.Date;

import io.skysail.api.forms.Field;
import io.skysail.domain.Identifiable;
import io.skysail.server.forms.PostView;
import lombok.*;

@Getter
@Setter
public class AThingWithSingleTabDefinition implements Identifiable {

    private String id;

    @Field
    @PostView(tab = "theTab")
    private Date created;

    @Field
    private String stringField;
    

}