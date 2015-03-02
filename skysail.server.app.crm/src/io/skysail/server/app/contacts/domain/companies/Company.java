package io.skysail.server.app.contacts.domain.companies;

import javax.validation.constraints.Size;

import lombok.Data;
import de.twenty11.skysail.api.forms.Field;
import de.twenty11.skysail.api.forms.InputType;
import de.twenty11.skysail.api.forms.ListView;

@Data
public class Company {

    @Field
    @Size(min = 1)
    private String name;

    @Field(type = InputType.READONLY, listView = ListView.HIDE)
    private String owner;

}
