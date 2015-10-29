package io.skysail.server.domain.core.test;

import io.skysail.api.domain.Identifiable;
import lombok.*;

@Getter
@Setter
public class AThing implements Identifiable {
    private String id;
}