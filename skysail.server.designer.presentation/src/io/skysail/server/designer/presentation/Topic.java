package io.skysail.server.designer.presentation;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.*;
import io.skysail.server.codegen.annotations.GenerateResources;

import javax.persistence.Id;
import javax.validation.constraints.*;

import lombok.*;

@Getter
@Setter
@GenerateResources(application = "io.skysail.server.designer.presentation.InteractivePresentationApplication")
public class Topic implements Identifiable {

    @Id
    private String id;

    @Field
    @NotNull
    @Size(min = 2)
    private String topic;

    @Field(inputType = InputType.TRIX_EDITOR, htmlPolicy = HtmlPolicy.DEFAULT_HTML)
    private String content;

}
