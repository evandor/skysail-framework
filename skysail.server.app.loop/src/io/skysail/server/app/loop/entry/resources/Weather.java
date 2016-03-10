package io.skysail.server.app.loop.entry.resources;

import io.skysail.domain.Identifiable;
import lombok.Data;

@Data
public class Weather implements Identifiable {

    private String id;
}
