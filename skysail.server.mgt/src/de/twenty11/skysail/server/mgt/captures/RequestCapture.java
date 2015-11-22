package de.twenty11.skysail.server.mgt.captures;

import io.skysail.api.domain.Identifiable;
import lombok.*;

@Getter
@Setter
public class RequestCapture implements Identifiable {

    private String id;
    private Integer count;
    private String key;

    public RequestCapture(String key, Integer count) {
        this.key = key;
        this.count = count;
    }

}
