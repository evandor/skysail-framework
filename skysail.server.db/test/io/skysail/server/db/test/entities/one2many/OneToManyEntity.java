package io.skysail.server.db.test.entities.one2many;

import io.skysail.api.domain.Identifiable;

import java.util.*;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class OneToManyEntity implements Identifiable {

    public OneToManyEntity(String name) {
        this.name = name;
    }

    private String id;

    private String name;

    private List<ToMany> toManies = new ArrayList<>();


}
