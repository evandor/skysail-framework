package io.skysail.server.app.designer.fields;

import io.skysail.api.forms.Field;

<<<<<<< Updated upstream
import java.util.Collections;
import java.util.Map;
=======
import java.util.*;
>>>>>>> Stashed changes

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
        return type == null ? Collections.emptyMap() : type.getCodes();
    }

}
