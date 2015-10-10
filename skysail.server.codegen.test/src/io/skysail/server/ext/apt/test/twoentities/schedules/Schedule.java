package io.skysail.server.ext.apt.test.twoentities.schedules;

import io.skysail.api.forms.Field;
import lombok.*;
import de.twenty11.skysail.server.ext.apt.annotations.*;

@GenerateListResource
@GeneratePutResource
@GeneratePostResource
@GenerateEntityResource
@Getter
@Setter
public class Schedule {

    @Field
    private String title;
}
