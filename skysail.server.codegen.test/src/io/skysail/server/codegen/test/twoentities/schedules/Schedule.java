package io.skysail.server.codegen.test.twoentities.schedules;

import io.skysail.api.forms.Field;
import lombok.*;

@Getter
@Setter
public class Schedule {

    @Field
    private String title;
}
