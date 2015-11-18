package io.skysail.server.db.impl.test.entities;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.*;
import io.skysail.server.db.OutEdges;
import lombok.*;

@Data
public class SomeUser implements Identifiable {

    private String id;

    @Field(inputType = InputType.MULTISELECT)
    private OutEdges<SomeRole> roles = new OutEdges<>();

    private String username;

    private String nonMandatoryField;

    public SomeUser(@NonNull String username) {
        this.username = username;
    }


}
