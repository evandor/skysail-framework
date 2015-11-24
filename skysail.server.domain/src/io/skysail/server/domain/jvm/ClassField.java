package io.skysail.server.domain.jvm;



public class ClassField extends io.skysail.server.domain.core.Field {

    public ClassField(String id) {
        super(id);
    }

    public ClassField(java.lang.reflect.Field f) {
        super(f.getName());
    }

}
