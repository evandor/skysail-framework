package io.skysail.server.app.designer.application;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import de.twenty11.skysail.api.forms.Field;

@NoArgsConstructor
@Getter
@Setter
public class Application2 {

    @Field
    @NotNull
    @Size(min = 1)
    private String name;
}
