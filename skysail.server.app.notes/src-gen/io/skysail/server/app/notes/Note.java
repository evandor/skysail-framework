package io.skysail.server.app.notes;

import javax.persistence.Id;

import java.util.ArrayList;
import java.util.List;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.*;
import io.skysail.server.forms.*;

import org.apache.commons.lang3.StringUtils;

public class Note implements Identifiable {

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

    @Field(inputType = InputType.TRIX_EDITOR, htmlPolicy = HtmlPolicy.TRIX_EDITOR)
    @ListView(hide = true)
    private String content;

    public void setContent(String value) {
        this.content = value;
    }

    public String getContent() {
        return this.content;
    }

    @Field(inputType = InputType.TEXT, htmlPolicy = HtmlPolicy.NO_HTML)
    @PostView(visibility = Visibility.HIDE)
    @PutView(visibility = Visibility.HIDE)
    private String title;

    public void setTitle(String value) {
        this.title = value;
    }

    public String getTitle() {
        return this.title;
    }

    @Field(inputType = InputType.DATE, htmlPolicy = HtmlPolicy.NO_HTML)
    @PostView(visibility = Visibility.HIDE)
    @PutView(visibility = Visibility.HIDE)
    private String modified;

    public void setModified(String value) {
        this.modified = value;
    }

    public String getModified() {
        return this.modified;
    }


    // --- relations ---



}