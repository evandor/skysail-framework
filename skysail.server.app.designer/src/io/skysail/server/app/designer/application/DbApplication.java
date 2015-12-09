package io.skysail.server.app.designer.application;

import java.util.*;

import javax.persistence.Id;
import javax.validation.constraints.*;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.*;
import io.skysail.server.app.designer.entities.DbEntity;
import io.skysail.server.forms.ListView;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString(of = { "id", "name" })
public class DbApplication implements Identifiable {

    @Id
    private String id;

    @Field
    @NotNull
    @Size(min = 1)
    @Pattern(regexp = "[a-zA-Z_]([a-zA-Z0-9_])*", message = "please choose a simpler Identifier. Some of the characters are not allowed.")
    private String name;

    @Field
    @NotNull
    @Size(min = 1)
    @Pattern(regexp = "[a-zA-Z_]([\\.\\w])*", message = "please choose a simpler Identifier like 'skysail.server.app.designer.myapp'. Some of the characters are not allowed.")
    private String projectName;

    @Field
    @NotNull
    @Size(min = 1)
    @Pattern(regexp = "[a-zA-Z_]([\\.\\w])*", message = "please choose a valid java package name like 'io.skysail.some.package'. Some of the provided characters are not allowed.")
    private String packageName;

    @Field
    @Size(min = 1)
    @NotNull
    private String path;

    @Field(inputType = InputType.READONLY)
    @ListView(hide = true)
    private String owner;

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
