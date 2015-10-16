package io.skysail.server.db.it;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.Field;

import javax.persistence.Id;

import lombok.*;

@Getter
@Setter
public class TestEntity implements Identifiable {

    @Id
    private String id;

    @Field
    private String name;
}
