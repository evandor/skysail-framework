package io.skysail.server.domain.jvm;

import io.skysail.api.domain.Identifiable;
import io.skysail.server.domain.core.Entity;
import io.skysail.server.domain.utils.*;

import java.lang.reflect.Field;
import java.util.function.Function;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import org.restlet.resource.ServerResource;

@Slf4j
@ToString
public class ClassEntity extends Entity {

    protected Class<? extends Identifiable> identifiableClass;

    public ClassEntity(Class<? extends Identifiable> identifiableClass) {
        super(identifiableClass.getSimpleName());
        this.identifiableClass = identifiableClass;
        deriveFields(identifiableClass);
    }

    public Class<? extends ServerResource> getPostResourceClass() {
        return getClass(identifiableClass.getPackage().getName() + ".Post" + getId() + "Resource");
    }

    public Class<? extends ServerResource> getPutResourceClass() {
        return getClass(identifiableClass.getPackage().getName() + ".Put" + getId() + "Resource");
    }

    public Class<? extends ServerResource> getListResourceClass() {
        return getClass(identifiableClass.getPackage().getName() + "." + getId() + "sResource");
    }

    public Class<? extends ServerResource> getEntityResourceClass() {
        return getClass(identifiableClass.getPackage().getName() + "." + getId() + "Resource");
    }

    @SuppressWarnings("unchecked")
    private Class<? extends ServerResource> getClass(String classname) {
        try {
            log.debug("searching for class '{}'", classname);
            return (Class<? extends ServerResource>) Class.forName(classname,false,identifiableClass.getClassLoader());
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(),e);
            return null;
        }
    }

    private void deriveFields(Class<? extends Identifiable> cls) {
        setFields(ReflectionUtils.getInheritedFields(cls).stream()
            .filter(f -> filterFormFields(f))
            .map(f -> new ClassField(f))
            .collect(MyCollectors.toLinkedMap(ClassField::getId, Function.identity())));
    }

    private boolean filterFormFields(Field f) {
        io.skysail.api.forms.Field formFieldAnnotation = f.getAnnotation(io.skysail.api.forms.Field.class);
        return formFieldAnnotation != null;
    }


}
