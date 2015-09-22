package io.skysail.server.repo;

import io.skysail.api.domain.Identifiable;

public interface Repository {

    Object save (Identifiable identifiable);

    Object update(String id, Object entity, String... edges);

}
