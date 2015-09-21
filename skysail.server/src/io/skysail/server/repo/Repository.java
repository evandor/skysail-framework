package io.skysail.server.repo;

import io.skysail.api.domain.Identifiable;

public interface Repository {

    Object save (Identifiable identifiable);
}
