package io.skysail.server.app.crm.contacts;

import io.skysail.server.app.crm.CrmEntity;
import io.skysail.server.app.crm.Salutation;
import io.skysail.server.app.crm.SalutationSelectionProvider;
import io.skysail.server.app.crm.companies.Company;
import io.skysail.server.app.crm.companies.resources.CompanySelectionProvider;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.skysail.api.forms.Field;
import io.skysail.api.forms.InputType;
import io.skysail.api.forms.Reference;

@NoArgsConstructor
@Getter
@Setter
public class Contact extends CrmEntity {

    public Contact(String creator) {
        super(creator);
    }

    @Field(selectionProvider = SalutationSelectionProvider.class)
    private Salutation salutation;

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

    @Field
    private String title;

    @Field
    private String mobile;

    @Field
    private String department;

    // do not call, reports to, lead source, campaign, language pref, assigned
    // to, teams

}
