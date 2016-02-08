package io.skysail.domain.core.repos;

import io.skysail.domain.Identifiable;
import io.skysail.domain.core.ApplicationModel;

public interface Repository {

    Class<? extends Identifiable> getRootEntity();

    Identifiable findOne (String id);

    Object save (Identifiable identifiable, ApplicationModel applicationModel);

    Object update(Identifiable entity, ApplicationModel applicationModel);
    
    void delete(Identifiable identifiable);

}
