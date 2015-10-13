package io.skysail.server.app.um.db.domain;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.*;

import java.util.Date;

import javax.persistence.Id;

import lombok.*;

@Getter
@Setter
public abstract class AbstractUmEntity implements Identifiable {

    @Id
    private String id;

    @Field(inputType = InputType.READONLY)
    private Date created;

    @Field(inputType = InputType.READONLY)
    private Date modified;

    @Field(inputType = InputType.READONLY)
    private String modifiedBy;

}
