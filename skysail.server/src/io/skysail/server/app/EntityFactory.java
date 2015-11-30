package io.skysail.server.app;

import io.skysail.api.domain.Identifiable;
import io.skysail.server.domain.core.EntityModel;
import io.skysail.server.domain.jvm.ClassEntity;

public class EntityFactory {

    public static EntityModel createFrom(Class<? extends Identifiable> cls) {
        return new ClassEntity(cls);
    }

}
