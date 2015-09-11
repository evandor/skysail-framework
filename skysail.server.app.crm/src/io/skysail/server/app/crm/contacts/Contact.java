package io.skysail.server.app.crm.contacts;

import io.skysail.api.forms.*;
import io.skysail.server.app.crm.*;
import io.skysail.server.app.crm.companies.resources.CompanySelectionProvider;

import java.util.List;

import javax.validation.constraints.*;

import lombok.*;

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

    @Field(inputType = InputType.EMAIL)
    private String email;

    @Reference(selectionProvider = CompanySelectionProvider.class)//, cls = Company.class)
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
