package io.skysail.server.app.designer.application;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.*;
import lombok.*;

@Getter
@Setter
public class ImportDefinition implements Identifiable {

    private String id;
    
    @Field(inputType = InputType.TEXTAREA, htmlPolicy = HtmlPolicy.DEFAULT_HTML)
    private String yamlImport;
}
