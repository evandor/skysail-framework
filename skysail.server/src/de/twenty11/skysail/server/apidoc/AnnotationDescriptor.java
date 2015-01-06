package de.twenty11.skysail.server.apidoc;

import java.lang.annotation.Annotation;

public class AnnotationDescriptor {

    private String name;

    public AnnotationDescriptor(Annotation annotation) {
        name = annotation.annotationType().getName();
        annotation.annotationType();
    }

    public String getName() {
        return name;
    }

}
