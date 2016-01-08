package io.skysail.domain.core;

import java.util.*;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * A central class of skysail's core domain: An entity belongs to exactly one application
 * and aggregates {@link FieldModel}s describing the entities state along with a couple of
 * resource classes dealing with creation, alteration, reading and deleting this state.
 *
 */
@Getter
public class EntityModel {

    /** ID should be the full qualified java class name, i.e. io.skysail.entity.Customer */
    private final String id;

    @Setter
    /** the entities fields in a map with their id as key. */
    private Map<String, FieldModel> fields = new HashMap<>();
    
    @Setter
    /** names of related entities. */
    private List<EntityRelation> relations = new ArrayList<>();

    @Setter
    /** should this entity be treated as "Aggregate" (DDD)" */
    private boolean aggregate = true;

    public EntityModel(@NonNull String fullQualifiedClassName) {
        this.id = fullQualifiedClassName;
    }

    public EntityModel add(@NonNull FieldModel field) {
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
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName()).append(": ");
        sb.append(id).append("\n");
        sb.append("Fields: \n");
        fields.keySet().stream().forEach(
                key -> sb.append(" - ").append(key).append(":\n     ").append(fields.get(key).toString()).append("\n")
        );
        return sb.toString();
    }

}
