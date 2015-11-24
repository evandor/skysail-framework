package io.skysail.server.app;

import io.skysail.api.domain.Identifiable;
import io.skysail.server.domain.core.Entity;
import io.skysail.server.domain.jvm.ClassEntity;

public class EntityFactory {

    public static Entity createFrom(Class<? extends Identifiable> cls) {
        return new ClassEntity(cls);
    }

}
