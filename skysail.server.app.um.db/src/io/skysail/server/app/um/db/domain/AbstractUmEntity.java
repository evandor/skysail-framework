package io.skysail.server.app.um.db.domain;

import java.util.Date;

import javax.persistence.Id;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.*;
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
