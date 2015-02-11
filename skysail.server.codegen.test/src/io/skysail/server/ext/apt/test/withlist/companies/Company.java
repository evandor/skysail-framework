package io.skysail.server.ext.apt.test.withlist.companies;

import de.twenty11.skysail.api.forms.Field;
import de.twenty11.skysail.server.ext.apt.annotations.GenerateEntityResource;
import de.twenty11.skysail.server.ext.apt.annotations.GenerateListResource;
import de.twenty11.skysail.server.ext.apt.annotations.GeneratePostResource;
import de.twenty11.skysail.server.ext.apt.annotations.GeneratePutResource;
import lombok.Getter;
import lombok.Setter;

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
