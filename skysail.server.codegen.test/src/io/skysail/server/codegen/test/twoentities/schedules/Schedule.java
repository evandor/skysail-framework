package io.skysail.server.codegen.test.twoentities.schedules;

import io.skysail.domain.html.Field;
import lombok.*;

@Getter
@Setter
public class Schedule {

    @Field
    private String title;
}
