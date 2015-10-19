package io.skysail.server.db.impl.test.entities;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.*;
import lombok.Data;

@Data
public class SomeRole implements Identifiable {

    private String id;

    @Field(inputType = InputType.TEXT)
    private String rolename;

    public SomeRole(String rolename) {
        this.rolename = rolename;
    }
}