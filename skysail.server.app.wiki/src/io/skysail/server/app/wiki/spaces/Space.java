package io.skysail.server.app.wiki.spaces;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.*;
import io.skysail.server.app.wiki.pages.Page;
import io.skysail.server.forms.*;

import java.io.Serializable;
import java.util.*;

import javax.persistence.Id;
import javax.validation.constraints.*;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@UniquePerOwner
@ToString
public class Space implements Serializable, Identifiable {

    private static final long serialVersionUID = 5061219768727410582L;

    @Id
    @Field(type = InputType.READONLY)
    private String id;

    @Field
    @NotNull
    @Size(min = 2)
    private String name;

    @Field(type = InputType.READONLY)
    @ListView(hide=true)
    private String owner;
    
    @Reference(cls = Page.class)
    @PostView(visibility=Visibility.HIDE)
    @PutView(visibility=Visibility.HIDE)
    private List<Page> pages = new ArrayList<>();

    public void addPage(Page entity) {
        pages.add(entity);
    }

    public Space(String name) {
        this.name = name;
    }

}
