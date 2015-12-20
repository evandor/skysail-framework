package io.skysail.server.db.test.entities.simple;

import io.skysail.domain.Identifiable;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SimpleEntity implements Identifiable {

    private String id;

    private String name;

    public SimpleEntity(String name) {
        this.name = name;
    }
}
