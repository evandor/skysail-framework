package io.skysail.server.app.bb;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.Field;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AreaOld implements Identifiable {

    @Id
    private String id;
    
    @Field
    @Size(min = 2)
    @NotNull
    private String title;

    public AreaOld(String title) {
        this.title = title;
    }


}
