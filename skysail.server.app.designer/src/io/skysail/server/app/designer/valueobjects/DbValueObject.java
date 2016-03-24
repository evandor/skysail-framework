package io.skysail.server.app.designer.valueobjects;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.Relation;
import io.skysail.server.app.designer.application.DbApplication;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DbValueObject implements Identifiable {

    private String id;
    
    @Field
    @Pattern(regexp="[A-Z_$][a-zA-Z\\d_$]*", message = "Please start with an uppercase letter, and don't use any special characters.")
    @NotNull
    @Size(min=2)
    private String name;
    
    @JsonBackReference
    private DbApplication dbApplication;

    @Relation
    @JsonManagedReference
    private List<DbValueObjectElement> elements = new ArrayList<>();
    
}
