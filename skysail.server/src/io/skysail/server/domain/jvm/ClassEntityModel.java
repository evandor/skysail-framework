package io.skysail.server.domain.jvm;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.restlet.resource.ServerResource;

import io.skysail.api.domain.Identifiable;
import io.skysail.server.domain.core.EntityModel;
import io.skysail.server.utils.*;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ToString
public class ClassEntityModel extends EntityModel {

    protected Class<? extends Identifiable> identifiableClass;

    public ClassEntityModel(Class<? extends Identifiable> identifiableClass) {
        super(identifiableClass.getName());
        this.identifiableClass = identifiableClass;
        deriveFields(identifiableClass);
    }

    public Class<? extends ServerResource> getPostResourceClass() {
        if (identifiableClass.getPackage() == null) {
            return  getClass(packageOf(identifiableClass.getName()) + ".Post" + identifiableClass.getSimpleName() + "Resource");
        }
        return getClass(identifiableClass.getPackage().getName() + ".Post" + identifiableClass.getSimpleName() + "Resource");
    }

    public Class<? extends ServerResource> getPutResourceClass() {
        if (identifiableClass.getPackage() == null) {
            return  getClass(packageOf(identifiableClass.getName()) + ".Put" + identifiableClass.getSimpleName() + "Resource");
        }
        return getClass(identifiableClass.getPackage().getName() + ".Put" + identifiableClass.getSimpleName() + "Resource");
    }

    public Class<? extends ServerResource> getListResourceClass() {
        if (identifiableClass.getPackage() == null) {
            return getClass(identifiableClass.getName() + "sResource");
        }
        return getClass(identifiableClass.getPackage().getName() + "." + identifiableClass.getSimpleName() + "sResource");
    }

    public Class<? extends ServerResource> getEntityResourceClass() {
        if (identifiableClass.getPackage() == null) {
            return  getClass(identifiableClass.getName() + "Resource");
        }
        return getClass(identifiableClass.getPackage().getName() + "." + identifiableClass.getSimpleName() + "Resource");
    }

    @SuppressWarnings("unchecked")
    private Class<? extends ServerResource> getClass(String classname) {
        try {
            log.debug("searching for class '{}'", classname);
            return (Class<? extends ServerResource>) Class.forName(classname,false,identifiableClass.getClassLoader());
        } catch (ClassNotFoundException e) {
            log.info(e.getMessage());
            return null;
        }
    }

    private void deriveFields(Class<? extends Identifiable> cls) {
        setFields(ReflectionUtils.getInheritedFields(cls).stream()
            .filter(f -> filterFormFields(f))
            .map(f -> new ClassFieldModel(f))
            .collect(MyCollectors.toLinkedMap(ClassFieldModel::getId, Function.identity())));
    }

    private boolean filterFormFields(Field f) {
        io.skysail.api.forms.Field formFieldAnnotation = f.getAnnotation(io.skysail.api.forms.Field.class);
        return formFieldAnnotation != null;
    }
    
    private String packageOf(String fullQualifiedName) {
        String[] split = fullQualifiedName.split("\\.");
        return Arrays.stream(Arrays.copyOf(split, split.length-1)).collect(Collectors.joining("."));
    }


}
