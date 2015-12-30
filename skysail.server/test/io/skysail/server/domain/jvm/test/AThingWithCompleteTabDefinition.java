package io.skysail.server.domain.jvm.test;

import java.util.Date;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import io.skysail.server.forms.PostView;
import lombok.*;

@Getter
@Setter
public class AThingWithCompleteTabDefinition implements Identifiable {

    private String id;

    @Field
    @PostView(tab = "theTab")
    private String stringField;
    
    @Field
    @PostView(tab = "optional")
    private Date created;


}