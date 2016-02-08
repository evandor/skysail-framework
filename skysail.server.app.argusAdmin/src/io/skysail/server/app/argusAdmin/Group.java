package io.skysail.server.app.argusAdmin;

import javax.persistence.Id;

import java.util.ArrayList;
import java.util.List;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.*;
import io.skysail.server.forms.*;

import org.apache.commons.lang3.StringUtils;

public class Group implements Identifiable {

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
    private String name;

    public void setName(String value) {
        this.name = value;
    }

    public String getName() {
        return this.name;
    }

    @Field(inputType = InputType.TEXTAREA, htmlPolicy = HtmlPolicy.NO_HTML)
    private String description;

    public void setDescription(String value) {
        this.description = value;
    }

    public String getDescription() {
        return this.description;
    }


    // --- relations ---



}