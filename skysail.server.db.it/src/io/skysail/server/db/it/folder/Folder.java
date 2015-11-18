package io.skysail.server.db.it.folder;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.*;
import io.skysail.server.db.OutEdges;

import java.util.Date;

import javax.persistence.Id;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A recursive folder structure.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
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
    private OutEdges<Folder> subfolders = new OutEdges<>();

}
