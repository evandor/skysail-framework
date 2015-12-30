package io.skysail.server.db.impl.test;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Id;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.*;
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
