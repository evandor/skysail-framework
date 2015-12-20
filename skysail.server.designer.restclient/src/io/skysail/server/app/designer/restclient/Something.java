package io.skysail.server.app.designer.restclient;

import io.skysail.api.forms.Field;
import io.skysail.domain.Identifiable;
import lombok.*;

@Getter
@Setter
public class Something implements Identifiable {

    private String id;

    @Field
    private String msg;


    public Something(String msg) {
        this.msg = msg;
    }
}
