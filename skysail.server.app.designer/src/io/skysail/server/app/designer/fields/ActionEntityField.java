package io.skysail.server.app.designer.fields;

import io.skysail.api.forms.Field;

import java.util.*;

import javax.persistence.Id;
import javax.validation.constraints.*;

import lombok.*;

@Getter
@Setter
public class ActionEntityField {

    @Id
    private String id;

    @Field
    @NotNull
    @Size(min = 1)
    private String name;

    @Field(selectionProvider = ActionTypeSelectionProvider.class)
    private ActionType type;

    public Map<String, String> getCodes() {
        return type != null ? type.getCodes() : Collections.emptyMap();
    }

}
