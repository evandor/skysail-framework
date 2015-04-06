package io.skysail.server.documentation;

import io.skysail.server.restlet.resources.ListServerResource;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class EntityDescriptor {

    private String name;
    private String simpleName;
    private Class<?> rawType;
    private List<AnnotationDescriptor> annotationDescriptors = new ArrayList<AnnotationDescriptor>();
    private List<FieldDescriptor> fieldDescriptors = new ArrayList<FieldDescriptor>();

    public EntityDescriptor(Class<?> entityClass, Class<?> rawType) {
        this.rawType = rawType; // e.g. class
                                // de.twenty11.skysail.server.core.restlet.ListServerResource
        name = entityClass.getName(); // de.twenty11.skysail.server.app.clipboard.clip.Clip
        simpleName = entityClass.getSimpleName(); // e.g. Clip
        setUpClassAnnotations(entityClass.getAnnotations());
        setUpFieldAnnotations(entityClass);
    }

    public String getName() {
        if (rawType.getName().equals(ListServerResource.class.getName())) {
            return "List&lt;" + name + "&gt;";
        }
        return name;
    }

    public String getSimpleName() {
        if (rawType.getName().equals(ListServerResource.class.getName())) {
            return "List&lt;" + simpleName + "&gt;";
        }
        return simpleName;
    }

    private void setUpClassAnnotations(Annotation[] annotations) {
        if (annotations != null) {
            for (Annotation annotation : annotations) {
                annotationDescriptors.add(new AnnotationDescriptor(annotation));
            }
        }
    }

    private void setUpFieldAnnotations(Class<?> entityClass) {
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            fieldDescriptors.add(new FieldDescriptor(field));
        }
    }

    public String getAnnotations() {
        StringBuilder sb = new StringBuilder();
        for (AnnotationDescriptor desc : annotationDescriptors) {
            sb.append(desc.getName()).append("<br>");
        }
        return sb.toString();
    }

    public List<FieldDescriptor> getFieldDescriptors() {
        return fieldDescriptors;
    }

    @Override
    public String toString() {
        return new StringBuilder().append(simpleName).toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EntityDescriptor other = (EntityDescriptor) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

}
