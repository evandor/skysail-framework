package io.skysail.server.app.cRM;

import io.skysail.domain.Identifiable;
import io.skysail.api.forms.*;
import io.skysail.server.forms.ListView;

import java.net.*;
import java.util.Date;

import javax.persistence.Id;

import org.apache.commons.lang3.StringUtils;

public class Contact implements Identifiable {

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
    private String surname;

    public void setSurname(String value) {
        this.surname = value;
    }

    public String getSurname() {
        return this.surname;
    }

    @Field(inputType = InputType.TEXT, htmlPolicy = HtmlPolicy.NO_HTML)
    private String name;

    public void setName(String value) {
        this.name = value;
    }

    public String getName() {
        return this.name;
    }




}