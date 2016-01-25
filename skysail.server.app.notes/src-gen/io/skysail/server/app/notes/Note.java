package io.skysail.server.app.notes;

import javax.persistence.Id;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.HtmlPolicy;
import io.skysail.domain.html.InputType;
import io.skysail.server.forms.ListView;
import io.skysail.server.forms.PostView;
import io.skysail.server.forms.PutView;
import io.skysail.server.forms.Visibility;

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


    // --- relations ---



}