package io.skysail.server.testsupport;

import org.restlet.data.Form;

public class FormBuilder {
    
    private Form form;

    public FormBuilder() {
        form = new Form();
    }

    public FormBuilder add(String key, String value) {
        form.add(key, value);
        return this;
    }

    public Form build() {
        return form;
    }

}
