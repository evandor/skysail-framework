package io.skysail.server.codegen.test.withlist.companies;

import io.skysail.domain.html.Field;
import lombok.*;

@Getter
@Setter
public class Company {

    @Field
    private String name;
}
