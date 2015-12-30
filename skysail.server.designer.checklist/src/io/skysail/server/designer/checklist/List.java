package io.skysail.server.designer.checklist;

import javax.persistence.Id;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.*;

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

    @Field(inputType = InputType.TEXT, htmlPolicy = HtmlPolicy.NO_HTML)
    private String listname;

    public void setListname(String value) {
        this.listname = value;
    }

    public String getListname() {
        return this.listname;
    }




}