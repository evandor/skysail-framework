package io.skysail.server.app;

import io.skysail.domain.Identifiable;
import io.skysail.domain.core.EntityModel;
import io.skysail.server.domain.jvm.ClassEntityModel;

public class EntityFactory {

    public static EntityModel createFrom(Class<? extends Identifiable> cls) {
        return new ClassEntityModel(cls);
    }

}
