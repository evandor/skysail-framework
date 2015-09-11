package io.skysail.server.app.crm.leads;

import io.skysail.api.forms.*;
import io.skysail.server.app.crm.*;

import javax.validation.constraints.*;

public class Lead {

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

    // title, mobile, website, donotcall, account, lead score
}
