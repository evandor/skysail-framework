package io.skysail.server.app.crm.domain;

import java.util.Date;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import de.twenty11.skysail.api.forms.Field;
import de.twenty11.skysail.api.forms.InputType;
import de.twenty11.skysail.api.forms.ListView;

@Getter
@Setter
public class CrmEntity {

    @Id
    private Object id;

    public CrmEntity() {
    }

    public CrmEntity(String creator) {
        this.creator = creator;
        this.owner = creator;
        this.created = new Date();
    }

    @Field(type = InputType.READONLY, listView = ListView.HIDE)
    @NotNull
    protected String creator;

    @Field(type = InputType.READONLY, listView = ListView.HIDE)
    protected Date created;

    @Field(type = InputType.READONLY)
    protected Date changed;

    @Field(type = InputType.READONLY, listView = ListView.HIDE)
    protected String changedBy;

    @Field(type = InputType.READONLY, listView = ListView.HIDE)
    protected String owner;

}
