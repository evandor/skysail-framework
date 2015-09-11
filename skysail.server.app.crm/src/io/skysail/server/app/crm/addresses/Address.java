package io.skysail.server.app.crm.addresses;

import io.skysail.api.forms.Field;
import lombok.*;


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
