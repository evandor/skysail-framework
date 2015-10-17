package io.skysail.server.db.it.clip;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.Field;

import java.util.Date;

import javax.persistence.Id;

import lombok.*;

/**
 * A very simple entity, without validation, being able to
 * track its own creation and modification timestamps.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Clip implements Identifiable {

    public Clip(String title) {
        this.title = title;
    }

    @Id
    private String id;

    @Field
    private String title;

    @Field
    private Date created;

    @Field
    private Date modified;

}
