package io.skysail.server.codegen.test.withlist.companies;

import io.skysail.api.forms.Field;
import lombok.*;
import de.twenty11.skysail.server.ext.apt.annotations.*;

@GenerateEntityResource
@GenerateListResource
@GeneratePostResource
@GeneratePutResource
@Getter
@Setter
public class Company {

    @Field
    private String name;
}
