package io.skysail.server.app.designer.application;

import io.skysail.server.app.designer.entities.Entity;

import java.util.List;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import de.twenty11.skysail.api.forms.Field;
import de.twenty11.skysail.api.forms.Reference;

@NoArgsConstructor
@Getter
@Setter
public class Application {

    @Id
    private Object id;

    @Field
    @NotNull
    @Size(min = 1)
    private String name;

    @Reference(cls = Entity.class)
    private List<String> entities;
}
