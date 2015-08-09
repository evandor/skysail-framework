package io.skysail.server.documentation;


@lombok.Getter
public class EntityDescriptor {

    @io.skysail.api.forms.Field
    private String name;

    public EntityDescriptor(String name) {
        this.name = name;
    }

}
