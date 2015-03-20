package io.skysail.server.app.crm.leads;

import io.skysail.server.app.crm.Salutation;
import io.skysail.server.app.crm.SalutationSelectionProvider;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.skysail.api.forms.Field;
import io.skysail.api.forms.InputType;

public class Lead {

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

    // title, mobile, website, donotcall, account, lead score
}
