package de.twenty11.skysail.server.apidoc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class FieldDescriptor {

    private String name;
    private List<AnnotationDescriptor> annotationDescriptors = new ArrayList<AnnotationDescriptor>();
    private Class<?> type;

    public FieldDescriptor(Field field) {
        name = field.getName();
        type = field.getType();
        Annotation[] annotations = field.getAnnotations();
        for (Annotation annotation : annotations) {
            annotationDescriptors.add(new AnnotationDescriptor(annotation));
        }
    }

    public String getName() {
        return name;
    }

    public List<AnnotationDescriptor> getAnnotationDescriptors() {
        return annotationDescriptors;
    }

    public String getType() {
        return type.toString();
    }

    @Override
    public String toString() {
        return new StringBuilder(name).toString();
    }
}
