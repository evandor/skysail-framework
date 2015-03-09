package io.skysail.server.app.crm.addresses;

import lombok.Getter;
import lombok.Setter;
import de.twenty11.skysail.api.forms.Field;

@Getter
@Setter
public class Address {

    @Field
    private String streetNumber;

    @Field
    private String street;

    @Field
    private String locality;

    @Field
    private String country;

    @Field
    private String postalCode;

}
