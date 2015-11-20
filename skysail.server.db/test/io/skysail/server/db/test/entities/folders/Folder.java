package io.skysail.server.db.test.entities.folders;

import io.skysail.api.domain.Identifiable;

import java.util.*;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Folder implements Identifiable {

    private String id;

    private String name;

    private List<Folder> subfolder = new ArrayList<>();

    public Folder(String name) {
        this.name = name;
    }

}
