package io.skysail.server.domain.core;

import io.skysail.api.domain.Identifiable;
import io.skysail.server.domain.core.resources.*;

import java.util.*;

import lombok.*;

/**
 * A central class of skysail's core domain: An entity belongs to exactly one application
 * and aggregates {@link Field}s describing the entities state along with a couple of
 * resource classes dealing with creation, alteration, reading and deleting this state.
 *
 */
@Getter
@Setter
public class Entity implements Identifiable {

    private String id;
    private String packageName;
    private String simpleName;

    private Map<String, Field> fields = new HashMap<>();

    private PostResource<Entity> postResource;
    private PutResource<Entity> putResource;
    private ListResource<Entity> listResource;
    private EntityResource<Entity> entityResource;

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
        this.fields.put(field.getId(), field);
        return this;
    }

    public Set<String> getFieldNames() {
        return fields.keySet();
    }

    public Field getField(String identifier) {
        return fields.get(identifier);
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
