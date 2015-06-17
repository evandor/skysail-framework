package io.skysail.server.app.wiki.pages;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.*;
import io.skysail.server.app.wiki.spaces.Space;
import io.skysail.server.forms.*;

import java.io.Serializable;

import javax.persistence.Id;
import javax.validation.constraints.*;

import org.restlet.data.Form;

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

    @Field(type = InputType.READONLY)
    @ListView(hide = true)
    private String owner;

    @Reference(cls = Space.class)
    // , selectionProvider = ListSelectionProvider.class)
    @PostView(visibility = Visibility.SHOW_IF_NULL)
    // @ListView(hide = true)
    // @ValidListId
    private String space;

    public Page(String name) {
        this.name = name;
    }

    public Page(Form query, String spaceId) {
        this.space = spaceId;
    }

}
