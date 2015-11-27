package io.skysail.server.domain.core.resources;

import io.skysail.api.domain.Identifiable;
import io.skysail.server.domain.core.Link;

import java.util.List;

import lombok.*;

@Getter
@Setter
@ToString
public abstract class Resource<T extends Identifiable>  {

    private List<Link<?>> links;

}
