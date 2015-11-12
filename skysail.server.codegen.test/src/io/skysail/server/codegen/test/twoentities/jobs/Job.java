package io.skysail.server.codegen.test.twoentities.jobs;

import io.skysail.api.forms.Field;
import lombok.*;

@Getter
@Setter
public class Job {

    @Field
    private String title;
}
