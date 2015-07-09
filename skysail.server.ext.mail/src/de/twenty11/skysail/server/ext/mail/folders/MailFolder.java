package de.twenty11.skysail.server.ext.mail.folders;

import io.skysail.api.forms.Field;

public class MailFolder {

    @Field
    private String name;

    public MailFolder(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
