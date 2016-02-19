package io.skysail.server.services;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ThemeDefinition {

    private final String name, version, desc;

    private boolean selected;
}
