package io.skysail.server.db.impl.test;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.Field;

import java.util.*;

import javax.persistence.Id;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Goal implements Identifiable {

    @Id
    private String id;

    @Field
    private String name;

//    @Field(inputType = InputType.TEXTAREA)
//    @ListView(truncate = 60)
//    private String remarks;
//
//    @Field
//    private BigDecimal beginn = new BigDecimal(0);
//
//    @Field
//    private BigDecimal target = new BigDecimal(10);

    private List<Achievement> achievements = new ArrayList<>();

//    @Field(inputType = InputType.READONLY)
//    @ListView(hide=true)
//    private String owner = "#1";

}
