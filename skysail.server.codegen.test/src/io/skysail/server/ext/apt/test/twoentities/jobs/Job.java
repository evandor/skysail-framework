package io.skysail.server.ext.apt.test.twoentities.jobs;

import lombok.Getter;
import lombok.Setter;
import de.twenty11.skysail.api.forms.Field;
import de.twenty11.skysail.server.ext.apt.annotations.GenerateEntityResource;
import de.twenty11.skysail.server.ext.apt.annotations.GenerateListResource;
import de.twenty11.skysail.server.ext.apt.annotations.GeneratePostResource;
import de.twenty11.skysail.server.ext.apt.annotations.GeneratePutResource;

@GenerateListResource
@GeneratePutResource
@GeneratePostResource
@GenerateEntityResource
@Getter
@Setter
public class Job {

    @Field
    private String title;
}
