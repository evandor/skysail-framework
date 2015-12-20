package io.skysail.server.db.test.entities.one2many;

import io.skysail.domain.Identifiable;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ToMany implements Identifiable {

    private String id;

    private String name;

    public ToMany(String name) {
        this.name = name;
    }
}
