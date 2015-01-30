package io.skysail.server.documentation;

import java.lang.annotation.Annotation;

public class AnnotationDescriptor {

    private String name;

    /**
     * @param annotation
     */
    public AnnotationDescriptor(Annotation annotation) {
        name = annotation.annotationType().getName(); // e.g.
                                                      // javax.persistence.Id,
                                                      // de.twenty11.skysail.api.forms.Field
        annotation.annotationType();
    }

    public String getName() {
        return name;
    }

}
