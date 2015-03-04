package io.skysail.server.app.crm.domain;

import java.util.Date;

import javax.validation.constraints.NotNull;

import lombok.Data;
import de.twenty11.skysail.api.forms.Field;
import de.twenty11.skysail.api.forms.InputType;
import de.twenty11.skysail.api.forms.ListView;

@Data
public class CrmEntity {

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

    @Field(type = InputType.READONLY, listView = ListView.HIDE)
    protected String owner;

}
