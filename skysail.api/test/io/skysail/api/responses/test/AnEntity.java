package io.skysail.api.responses.test;

import io.skysail.domain.Identifiable;
import lombok.Data;

@Data
public class AnEntity implements Identifiable{
    private String id;
}