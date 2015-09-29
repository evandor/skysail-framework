package io.skysail.server.app.bb.achievements;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Id;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.Field;
import io.skysail.api.forms.InputType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DummyAchievement implements Identifiable {

    @Id
    private String id;
    
    @Field(inputType = InputType.DATE)
    private Date date;
    
    @Field
    private BigDecimal value;
}
