package io.skysail.server.ext.apt.test.simple.todos;

import io.skysail.api.forms.Field;
import lombok.*;
import de.twenty11.skysail.server.ext.apt.annotations.*;

/**
 * A simple Model containing one simple Entity.
 *
 */
@GenerateListResource
@GeneratePutResource
@GeneratePostResource
@GenerateEntityResource
@Getter
@Setter
public class Todo {

    @Field
    private String title;
}
