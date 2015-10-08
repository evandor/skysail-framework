package io.skysail.server.db.impl.test;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.*;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Id;

import lombok.*;

@Getter
@Setter
@ToString
public class Achievement implements Identifiable {

    @Id
    private String id;

    @Field(inputType = InputType.DATE)
    private Date date;

    @Field
    private BigDecimal value;
}
