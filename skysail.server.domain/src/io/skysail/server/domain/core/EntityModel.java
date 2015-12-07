package io.skysail.server.domain.core;

import java.util.*;

import lombok.*;

/**
 * A central class of skysail's core domain: An entity belongs to exactly one application
 * and aggregates {@link FieldModel}s describing the entities state along with a couple of
 * resource classes dealing with creation, alteration, reading and deleting this state.
 *
 */
@Getter
@ToString
public class EntityModel {

    /**
     * ID should be the full qualified java class name, i.e. io.skysail.entity.Customer
     */
    private final String id;

    /** the entities fields in a map with their id as key. */
    @Setter
    private Map<String, FieldModel> fields = new HashMap<>();

//    private PostResource<?> postResource;
//    private PutResource<?> putResource;
//    private ListResource<?> listResource;
//    private EntityResource<?> entityResource;
    
    /** should this entity be treated as "Aggregate" (DDD)" */
    @Setter
    private boolean aggregate = true;

    public EntityModel(@NonNull String fullQualifiedClassName) {
        this.id = fullQualifiedClassName;
//        postResource = new PostResource<>();
//        putResource = new PutResource<>();
//        listResource = new ListResource<>();
//        entityResource = new EntityResource<>();
    }

    public EntityModel add(FieldModel field) {
        this.fields.put(field.getId(), field);
        return this;
    }

    public Set<String> getFieldNames() {
        return fields.keySet();
    }

    public FieldModel getField(String identifier) {
        return fields.get(identifier);
    }
    
    public Collection<FieldModel> getFieldValues() {
        return fields.values();
    }

    public String getPackageName() {
        int indexOfLastDot = id.lastIndexOf(".");
        if (indexOfLastDot < 0) {
            return "";
        }
        return id.substring(0,indexOfLastDot);
    }
    
    public String getSimpleName() {
        int indexOfLastDot = id.lastIndexOf(".");
        if (indexOfLastDot < 0) {
            return id;
        }
        return id.substring(indexOfLastDot+1);
    }
    
    public String getPostResourceClassName() {
        return getPackageName() + ".Post" + getSimpleName() + "Resource";
    }
}
