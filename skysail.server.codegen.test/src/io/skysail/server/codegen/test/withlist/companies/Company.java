package io.skysail.server.codegen.test.withlist.companies;

import io.skysail.api.forms.Field;
import lombok.*;

@Getter
@Setter
public class Company {

    @Field
    private String name;
}
