package io.skysail.server.ext.sse.domain;

import io.skysail.domain.Identifiable;
import lombok.*;

@Getter
@Setter
public class GuiMessage implements Identifiable {

    private String msg = "hi";

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {
    }

}
