package io.skysail.server.app.designer.application;

import java.util.*;

import javax.persistence.Id;
import javax.validation.constraints.*;

import io.skysail.domain.*;
import io.skysail.domain.html.*;
import io.skysail.server.app.designer.application.validation.UniqueName;
import io.skysail.server.app.designer.entities.DbEntity;
import io.skysail.server.app.designer.entities.resources.EntitiesResource;
import io.skysail.server.forms.*;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString(of = { "id", "name", "entities" })
@UniqueName
public class DbApplication implements Identifiable, Nameable {

    @Id
    private String id;

    @Field
    @NotNull
    @Size(min = 1)
    @Pattern(regexp = "[a-zA-Z_]([a-zA-Z0-9_])*", message = "please choose a simpler Identifier. Some of the characters are not allowed.")
    @ListView(link = EntitiesResource.class, truncate = 40)
    @PostView(tab = "new application")
    private String name;

    @Field
    // FIXME change regex to empty|...
    //@Pattern(regexp = "[a-zA-Z_]([\\.\\w])*", message = "please choose a simpler Identifier like 'skysail.server.app.designer.myapp'. Some of the characters are not allowed.")
    @ListView(hide = true)
    @PostView(tab = "details")
    private String projectName;

    @Field
    // FIXME change regex to empty|...
    //@Pattern(regexp = "[a-zA-Z_]([\\.\\w])*", message = "please choose a valid java package name like 'io.skysail.some.package'. Some of the provided characters are not allowed.")
    @ListView(hide = true)
    @PostView(tab = "details")
    private String packageName;

    @Field
    @ListView(hide = true)
    @PostView(tab = "details")
    private String path;

    @Field(inputType = InputType.READONLY)
    @ListView(hide = true)
    private String owner;

    @Relation
    private List<DbEntity> entities = new ArrayList<>();

    /**
     * A builder which sets the mandatory attributes
     */
    @Builder
    public DbApplication(@NonNull String name, @NonNull String packageName, @NonNull String path, @NonNull String projectName) {
        this.name = name;
        this.packageName = packageName;
        this.path = path;
        this.projectName = projectName;
    }

}
