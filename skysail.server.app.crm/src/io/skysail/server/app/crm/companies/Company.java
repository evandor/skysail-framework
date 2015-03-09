package io.skysail.server.app.crm.companies;

import io.skysail.server.app.crm.CrmEntity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import de.twenty11.skysail.api.forms.Field;
import de.twenty11.skysail.api.forms.InputType;
import de.twenty11.skysail.api.forms.ListView;

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

    @Field(selectionProvider = IndustryTypeSelectionProvider.class, listView = ListView.HIDE)
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
