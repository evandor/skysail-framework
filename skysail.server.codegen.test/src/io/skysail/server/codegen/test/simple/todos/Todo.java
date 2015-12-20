package io.skysail.server.codegen.test.simple.todos;

import io.skysail.api.forms.Field;
import io.skysail.domain.Identifiable;
import io.skysail.server.codegen.ResourceType;
import io.skysail.server.codegen.annotations.GenerateResources;

import javax.persistence.Id;

import lombok.*;

/**
 * A simple Model containing one simple EntityModel.
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
