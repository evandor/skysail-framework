package de.twenty11.skysail.server.mgt.performance;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PerformanceInfo implements Identifiable{

    private String id;

    @Field
    private String info;

    public PerformanceInfo(String info) {
        this.info = info;
    }
}
