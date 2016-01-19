package io.skysail.server.app.wiki;

import javax.persistence.Id;

import java.util.ArrayList;
import java.util.List;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.*;
import io.skysail.server.forms.*;

import org.apache.commons.lang3.StringUtils;

public class Space implements Identifiable {

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
    @ListView(link = PagesResource.class)

    private String spacename;

    public void setSpacename(String value) {
        this.spacename = value;
    }

    public String getSpacename() {
        return this.spacename;
    }

    @Field(inputType = InputType.TEXT, htmlPolicy = HtmlPolicy.NO_HTML)
    @ListView(link = PagesResource.class)

    private String description;

    public void setDescription(String value) {
        this.description = value;
    }

    public String getDescription() {
        return this.description;
    }


    // --- relations ---

    @Relation
    private List<io.skysail.server.app.wiki.Page> pages = new ArrayList<>();

    public void setPages(List<io.skysail.server.app.wiki.Page> value) {
        this.pages = value;
    }

    public List<io.skysail.server.app.wiki.Page> getPages() {
        return pages;
    }




}