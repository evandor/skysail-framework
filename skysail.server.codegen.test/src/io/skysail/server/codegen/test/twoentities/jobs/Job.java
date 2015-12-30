package io.skysail.server.codegen.test.twoentities.jobs;

import io.skysail.domain.html.Field;
import lombok.*;

@Getter
@Setter
public class Job {

    @Field
    private String title;
}
