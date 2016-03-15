package io.skysail.server.app.designer.valueobjects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValueObjectElement implements Identifiable {

    private String id;
    
    @Field
    @Pattern(regexp="[a-zA-Z\\d_$]*", message = "Please do not use spaces or any other special characters.")
    @NotNull
    @Size(min=2)
    private String value;
    
    @Field
    private int order;
    
}
