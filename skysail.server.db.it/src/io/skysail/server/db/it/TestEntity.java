package io.skysail.server.db.it;

import io.skysail.api.domain.Identifiable;

import javax.persistence.Id;

import lombok.*;

@Getter
@Setter
public class TestEntity implements Identifiable {

    @Id
    private String id;
}
