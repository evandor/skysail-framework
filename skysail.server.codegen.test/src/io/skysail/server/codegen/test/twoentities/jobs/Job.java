package io.skysail.server.codegen.test.twoentities.jobs;

import io.skysail.api.forms.Field;
import lombok.*;
import de.twenty11.skysail.server.ext.apt.annotations.*;

@GenerateListResource
@GeneratePutResource
@GeneratePostResource
@Getter
@Setter
public class Job {

    @Field
    private String title;
}
