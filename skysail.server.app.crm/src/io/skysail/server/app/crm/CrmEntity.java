package io.skysail.server.app.crm;

import java.util.Date;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import de.twenty11.skysail.api.forms.Field;
import de.twenty11.skysail.api.forms.InputType;
import de.twenty11.skysail.api.forms.ListView;

@Getter
@Setter
@NoArgsConstructor
public class CrmEntity {

    @Id
    private String id;

    public CrmEntity(String creator) {
        this.creator = creator;
        this.owner = creator;
        this.created = new Date();
    }

    @Field(type = InputType.READONLY, listView = ListView.HIDE)
    protected String creator;

    @NotNull
    // need this as method annotation, otherwise problem with validation
    // framework
    public String getCreator() {
        return creator;
    }

    @Field(type = InputType.READONLY, listView = ListView.HIDE)
    protected Date created;
    //
    @Field(type = InputType.READONLY)
    protected Date changed;

    @Field(type = InputType.READONLY, listView = ListView.HIDE)
    protected String changedBy;

    @Field(type = InputType.READONLY, listView = ListView.HIDE)
    protected String owner;

}
