package io.skysail.server.documentation;

import io.skysail.api.domain.Identifiable;


@lombok.Getter
public class EntityDescriptor implements Identifiable {

    @io.skysail.api.forms.Field
    private String name;

    public EntityDescriptor(String name) {
        this.name = name;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {
    }

}
