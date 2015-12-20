package io.skysail.server.db.impl.test.entities;

import io.skysail.api.forms.*;
import io.skysail.domain.Identifiable;

import java.util.*;

import lombok.*;

@Data
public class SomeUser implements Identifiable {

    private String id;

    @Field(inputType = InputType.MULTISELECT)
    private List<SomeRole> roles = new ArrayList<>();

    private String username;

    private String nonMandatoryField;

    public SomeUser(@NonNull String username) {
        this.username = username;
    }


}
