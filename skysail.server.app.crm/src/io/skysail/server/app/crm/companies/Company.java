package io.skysail.server.app.crm.companies;

import io.skysail.api.forms.Field;
import io.skysail.api.forms.InputType;
import io.skysail.server.app.crm.CrmEntity;
import io.skysail.server.app.crm.companies.resources.CompanyType;
import io.skysail.server.app.crm.companies.resources.CompanyTypeSelectionProvider;
import io.skysail.server.app.crm.companies.resources.IndustryType;
import io.skysail.server.app.crm.companies.resources.IndustryTypeSelectionProvider;
import io.skysail.server.forms.ListView;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Field(type = InputType.URL)
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

    @Field(type = InputType.EMAIL)
    private String email;

    // @Reference(cls = Address.class)
    // private String billingAddress;

    // @Reference(cls = Address.class)
    // private String shippingAddress;

    @Field(type = InputType.TEXTAREA)
    private String description;

}
