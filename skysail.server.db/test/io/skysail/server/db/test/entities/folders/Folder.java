package io.skysail.server.db.test.entities.folders;

import java.util.*;

import io.skysail.domain.Identifiable;
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
