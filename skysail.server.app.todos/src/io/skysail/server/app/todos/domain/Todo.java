package io.skysail.server.app.todos.domain;

import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import de.twenty11.skysail.api.forms.Field;
import de.twenty11.skysail.api.forms.InputType;
import de.twenty11.skysail.server.ext.apt.annotations.GenerateEntityResource;
import de.twenty11.skysail.server.ext.apt.annotations.GenerateListResource;
import de.twenty11.skysail.server.ext.apt.annotations.GeneratePostResource;
import de.twenty11.skysail.server.ext.apt.annotations.GeneratePutResource;

@GeneratePostResource
@GenerateListResource
@GeneratePutResource
@GenerateEntityResource
@Getter
@Setter
@ToString(of = { "title" })
public class Todo {

    @Field
    @Size(min = 2)
    private String title;

    @Field(type = InputType.TEXTAREA)
    private String desc;

}
