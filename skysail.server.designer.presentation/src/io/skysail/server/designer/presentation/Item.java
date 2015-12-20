package io.skysail.server.designer.presentation;

import io.skysail.api.forms.*;
import io.skysail.domain.Identifiable;
import io.skysail.server.forms.ListView;

import java.net.*;
import java.util.Date;

import javax.persistence.Id;

import org.apache.commons.lang3.StringUtils;

public class Item implements Identifiable {

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

    @Field(inputType = InputType.TEXTAREA, htmlPolicy = HtmlPolicy.NO_HTML)
    private String description;

    public void setDescription(String value) {
        this.description = value;
    }

    public String getDescription() {
        return this.description;
    }

    @Field(inputType = InputType.CHECKBOX, htmlPolicy = HtmlPolicy.NO_HTML)
    private String newone;

    public void setNewone(String value) {
        this.newone = value;
    }

    public String getNewone() {
        return this.newone;
    }

    @Field(inputType = InputType.TEXT, htmlPolicy = HtmlPolicy.NO_HTML)
    private String title;

    public void setTitle(String value) {
        this.title = value;
    }

    public String getTitle() {
        return this.title;
    }

    @Field(inputType = InputType.TRIX_EDITOR, htmlPolicy = HtmlPolicy.DEFAULT_HTML)
    private String info;

    public void setInfo(String value) {
        this.info = value;
    }

    public String getInfo() {
        return this.info;
    }




}