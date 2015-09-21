package de.twenty11.skysail.server.ext.mail.folders;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.Field;

public class MailFolder implements Identifiable {

    @Field
    private String name;

    public MailFolder(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {
    }

}
