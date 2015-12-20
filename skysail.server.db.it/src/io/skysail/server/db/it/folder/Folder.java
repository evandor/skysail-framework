package io.skysail.server.db.it.folder;

import io.skysail.api.forms.*;
import io.skysail.domain.Identifiable;

import java.util.*;

import javax.persistence.Id;

import lombok.*;

/**
 * A recursive folder structure.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Folder implements Identifiable {

    public Folder(String name) {
        this.name = name;
    }

    @Id
    private String id;

    @Field
    private String name;

    @Field
    private Date created;

    @Field
    private Date modified;

    @Field(inputType = InputType.HIDDEN)
    private List<Folder> subfolders = new ArrayList<>();
}
