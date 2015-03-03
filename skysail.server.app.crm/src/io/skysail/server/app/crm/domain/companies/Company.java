package io.skysail.server.app.crm.domain.companies;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import de.twenty11.skysail.api.forms.Field;
import de.twenty11.skysail.api.forms.InputType;
import de.twenty11.skysail.api.forms.ListView;

@Data
public class Company {

    @Field
    @Size(min = 1)
    @NotNull
    private String name;

    @Field(type = InputType.READONLY, listView = ListView.HIDE)
    @NotNull
    private String owner;

}
