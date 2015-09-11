package io.skysail.server.app.crm.companies;

import io.skysail.api.forms.*;
import io.skysail.server.app.crm.CrmEntity;
import io.skysail.server.app.crm.companies.resources.*;
import io.skysail.server.forms.ListView;

import javax.validation.constraints.*;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
public class Company extends CrmEntity {

    public Company(String creator) {
        super(creator);
    }

    @Field
    @Size(min = 1)
    @NotNull
    private String name;

    @Field(inputType = InputType.URL)
    private String website;

    @Field(selectionProvider = CompanyTypeSelectionProvider.class)
    private CompanyType type;

    @Field(selectionProvider = IndustryTypeSelectionProvider.class)
    @ListView(hide=true)
    private IndustryType industry;

    // private assignedTo, teams, campaign, memberof, SIC Code, Ticker Symbol,
    // Annual Revenue, Employees, other phone

    @Field
    private String officePhone;

    @Field(inputType = InputType.EMAIL)
    private String email;

    // @Reference(cls = Address.class)
    // private String billingAddress;

    // @Reference(cls = Address.class)
    // private String shippingAddress;

    @Field(inputType = InputType.TEXTAREA)
    private String description;

}
