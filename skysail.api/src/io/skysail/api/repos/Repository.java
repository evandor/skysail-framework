package io.skysail.api.repos;

import io.skysail.api.domain.Identifiable;

public interface Repository {

    Identifiable findOne (String id);

    Object save (Identifiable identifiable);

    Object update(String id, Object entity, String... edges);

}
