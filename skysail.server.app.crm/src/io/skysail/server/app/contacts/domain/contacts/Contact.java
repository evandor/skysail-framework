package io.skysail.server.app.contacts.domain.contacts;

import io.skysail.server.app.contacts.domain.companies.CompanySelectionProvider;

import javax.validation.constraints.Size;

import lombok.Data;
import de.twenty11.skysail.api.forms.Field;
import de.twenty11.skysail.api.forms.InputType;
import de.twenty11.skysail.api.forms.ListView;

@Data
public class Contact {

    @Field
    @Size(min = 1)
    private String lastname;

    @Field
    private String firstname;

    @Field(type = InputType.READONLY, listView = ListView.HIDE)
    private String owner;

    @Field(selectionProvider = CompanySelectionProvider.class)
    private String worksFor;
}
