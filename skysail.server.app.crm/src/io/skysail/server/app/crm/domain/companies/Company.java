package io.skysail.server.app.crm.domain.companies;

import io.skysail.server.app.crm.domain.CrmEntity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import de.twenty11.skysail.api.forms.Field;

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

}
