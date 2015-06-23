package io.skysail.server.app.wiki.pages;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.*;
import io.skysail.server.forms.ListView;

import java.io.Serializable;

import javax.persistence.Id;
import javax.validation.constraints.*;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
// @UniquePerOwner
public class Page implements Serializable, Identifiable {

    private static final long serialVersionUID = 5061219768727410582L;

    @Id
    private String id;

    @Field
    @NotNull
    @Size(min = 2)
    private String name;

    @Field(type = InputType.TEXTAREA)
    @ListView(hide=true)
    private String content;

    @Field(type = InputType.READONLY)
    @ListView(hide = true)
    private String owner;

    public Page(String name) {
        this.name = name;
    }

}
