package io.skysail.server.app.crm.domain.contacts;

import io.skysail.server.app.crm.domain.CrmEntity;
import io.skysail.server.app.crm.domain.companies.Company;
import io.skysail.server.app.crm.domain.companies.CompanySelectionProvider;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;
import de.twenty11.skysail.api.forms.Field;
import de.twenty11.skysail.api.forms.InputType;
import de.twenty11.skysail.api.forms.Reference;

@Data
@NoArgsConstructor
public class Contact extends CrmEntity {

    public Contact(String creator) {
        super(creator);
    }

    @Field
    @Size(min = 1)
    @NotNull
    private String lastname;

    @Field
    private String firstname;

    @Field(type = InputType.EMAIL)
    private String email;

    @Reference(selectionProvider = CompanySelectionProvider.class, cls = Company.class)
    private List<String> worksFor;

}
