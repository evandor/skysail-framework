package io.skysail.domain.core.repos;

import io.skysail.domain.Identifiable;
import io.skysail.domain.core.ApplicationModel;

public interface Repository {

    Class<? extends Identifiable> getRootEntity();

    Identifiable findOne (String id);

    Object save (Identifiable identifiable, ApplicationModel applicationModel);

    Object update(String id, Identifiable entity, String... edges);
    
    void delete(Identifiable identifiable);

}
