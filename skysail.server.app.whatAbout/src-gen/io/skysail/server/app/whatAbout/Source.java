package io.skysail.server.app.whatAbout;

import javax.persistence.Id;

import java.util.ArrayList;
import java.util.List;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.*;
import io.skysail.server.forms.*;

import org.apache.commons.lang3.StringUtils;

public class Source implements Identifiable {

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

    @Field(inputType = InputType.URL, htmlPolicy = HtmlPolicy.NO_HTML)
    private String url;

    public void setUrl(String value) {
        this.url = value;
    }

    public String getUrl() {
        return this.url;
    }


    // --- relations ---



}