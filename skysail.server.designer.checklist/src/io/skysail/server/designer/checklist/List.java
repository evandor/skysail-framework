package io.skysail.server.designer.checklist;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.*;
import io.skysail.server.forms.ListView;

import java.net.*;
import java.util.Date;

import javax.persistence.Id;

import org.apache.commons.lang3.StringUtils;

public class List implements Identifiable {

    @Id
    private String id;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    // --- fields ---

    @Field(inputType = InputType.TEXT)
    private String listname;

    public void setListname(String value) {
        this.listname = value;
    }

    public String getListname() {
        return this.listname;
    }




}