package io.skysail.server.domain.core;

import io.skysail.api.domain.Identifiable;

import java.util.*;

import lombok.*;

//@ToString
//@Slf4j
@Getter
@Setter
public class Entity implements Identifiable {

    protected Class<? extends Identifiable> identifiableClass;

    private String id;

    private List<Field> fields = new ArrayList<>();

    private PostResource<Entity> postResource;
    private PutResource<Entity> putResource;
    private ListResource<Entity> listResource;
    private EntityResource<Entity> entityResource;

    private String packageName;
    private String simpleName;

    public Entity(String fullQualifiedClassName) {
        this.id = fullQualifiedClassName;
        this.packageName = getPackageFromName(fullQualifiedClassName);
        this.simpleName = toSimpleName(fullQualifiedClassName);
        postResource = new PostResource<>();
        putResource = new PutResource<>();
        listResource = new ListResource<>();
        entityResource = new EntityResource<>();
    }

    public Entity add(Field field) {
        this.fields.add(field);
        return this;
    }

    public List<Field> getFields() {
        return Collections.unmodifiableList(fields);
    }

    public String getPostResourceClassName() {
        return packageName + ".Post" + simpleName + "Resource";
    }

    private String getPackageFromName(String name) {
        int indexOfLastDot = name.lastIndexOf(".");
        if (indexOfLastDot < 0) {
            return "";
        }
        return name.substring(0,indexOfLastDot);
    }

    private String toSimpleName(String fullQualifiedClassName) {
        int indexOfLastDot = fullQualifiedClassName.lastIndexOf(".");
        if (indexOfLastDot < 0) {
            return fullQualifiedClassName;
        }
        return fullQualifiedClassName.substring(indexOfLastDot+1);
    }
}
