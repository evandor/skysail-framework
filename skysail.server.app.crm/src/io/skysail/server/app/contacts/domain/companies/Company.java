package io.skysail.server.app.contacts.domain.companies;

import javax.validation.constraints.Size;

import lombok.Data;
import de.twenty11.skysail.api.forms.Field;
import de.twenty11.skysail.api.forms.InputType;
import de.twenty11.skysail.api.forms.ListView;
import de.twenty11.skysail.server.ext.apt.annotations.GenerateEntityResource;
import de.twenty11.skysail.server.ext.apt.annotations.GenerateListResource;
import de.twenty11.skysail.server.ext.apt.annotations.GeneratePostResource;
import de.twenty11.skysail.server.ext.apt.annotations.GeneratePutResource;

@Data
@GenerateEntityResource
@GenerateListResource
@GeneratePostResource
@GeneratePutResource
public class Company {

    @Field
    @Size(min = 1)
    private String name;

    @Field(type = InputType.READONLY, listView = ListView.HIDE)
    private String owner;

}
