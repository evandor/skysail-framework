package io.skysail.server.app.bb;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.*;
import io.skysail.server.app.bb.achievements.DummyAchievement;
import io.skysail.server.app.bb.areas.Area;
import io.skysail.server.forms.ListView;

import java.math.BigDecimal;
import java.util.*;

import javax.persistence.Id;
import javax.validation.constraints.*;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class DummyGoal implements Identifiable {

    @Id
    private String id;

    @Field
    @NotNull
    @Size(min = 2)
    private String name;

    @Field(inputType = InputType.TEXTAREA)
    @ListView(truncate = 60)
    private String remarks;

    @Field(inputType = InputType.DATE)
    private Date due;

    @Field
    private BigDecimal beginn = new BigDecimal(0);

    @Field
    private BigDecimal target = new BigDecimal(10);

    private List<DummyAchievement> achievements = new ArrayList<>();

    @Field(inputType = InputType.READONLY)
    @ListView(hide=true)
    private String owner = "#1";

    private Area area;

    public DummyGoal(String name, Area area) {
        this.name = name;
        this.area = area;
    }
}
