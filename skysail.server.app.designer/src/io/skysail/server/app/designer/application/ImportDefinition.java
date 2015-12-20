package io.skysail.server.app.designer.application;

import io.skysail.api.forms.*;
import io.skysail.domain.Identifiable;
import lombok.*;

@Getter
@Setter
public class ImportDefinition implements Identifiable {

    private String id;
    
    @Field(inputType = InputType.TEXTAREA, htmlPolicy = HtmlPolicy.DEFAULT_HTML)
    private String yamlImport;
}
