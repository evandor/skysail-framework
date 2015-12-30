package io.skysail.server.designer.presentation;

import javax.persistence.Id;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.*;

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

    @Field(inputType = InputType.TEXT, htmlPolicy = HtmlPolicy.NO_HTML)
    private String topic;

    public void setTopic(String value) {
        this.topic = value;
    }

    public String getTopic() {
        return this.topic;
    }




}