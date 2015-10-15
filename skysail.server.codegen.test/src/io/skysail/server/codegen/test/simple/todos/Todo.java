package io.skysail.server.codegen.test.simple.todos;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.Field;

import javax.persistence.Id;

import lombok.*;
import de.twenty11.skysail.server.ext.apt.annotations.*;

/**
 * A simple Model containing one simple Entity.
 *
 */
@Getter
@Setter
@GenerateResources(application = "io.skysail.server.codegen.test.simple.TodoApplication", exclude = {ResourceType.POST})
public class Todo implements Identifiable {

    @Id
    private String id;

    @Field
    private String title;
}
