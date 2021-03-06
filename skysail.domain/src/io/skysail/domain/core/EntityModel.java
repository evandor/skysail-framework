package io.skysail.domain.core;

import java.util.*;

import org.apache.commons.lang.StringUtils;

import lombok.*;

/**
 * A central class of skysail's core domain: An entity belongs to exactly one application
 * and aggregates {@link FieldModel}s describing the entities state along with a couple of
 * resource classes dealing with creation, alteration, reading and deleting this state.
 *
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EntityModel {

    /** ID should be the full qualified java class name, i.e. io.skysail.entity.Customer */
    private String id;

    @Setter
    /** the entities fields in a map with their id as key. */
    private Map<String, FieldModel> fields = new LinkedHashMap<>();
    
    @Setter
    /** names of related entities. */
    private List<EntityRelation> relations = new ArrayList<>();

    @Setter
    /** should this entity be treated as "Aggregate" (DDD)" */
    private boolean aggregate = true;

    @Setter
    /** should be set once an entity model is added to an application. */
    private ApplicationModel applicationModel;

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
    
    public EntityModel getAggregateRoot() {
        if (isAggregate()) {
            return this;
        }
        if (getApplicationModel() != null) {
            
            Optional<EntityModel> parentEntityModel = getApplicationModel().getEntityValues().stream().filter(entity -> {
                return entity.getRelations().stream().filter(relation -> {
                   return relation.getTargetEntityModel().equals(this); 
                }).findFirst().isPresent();
            }).findFirst();
            if (parentEntityModel.isPresent()) {
                return parentEntityModel.get().getAggregateRoot();
            }
            
//            List<EntityRelation> relations = getApplicationModel().getEntityValues().stream().map(entity -> entity.getRelations()).flatMap(r -> r.stream()).collect(Collectors.toList());
//            Optional<EntityRelation> optionalParent = relations.stream().filter(r -> r.getTargetEntityModel().equals(this)).findFirst();
//            if (optionalParent.isPresent()) {
//                return optionalParent.get().getTargetEntityModel().getAggregateRoot();
//            }
        }
        return null;
    }


    @Override
    public String toString() {
        return toString(0);
    }

    public String toString(int indentation) {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName()).append(": ");
        sb.append("id='").append(id).append("', isAggregate=").append(isAggregate()).append("\n");
        fieldsToString(sb);
        relationsToString(sb);
        return sb.toString();
    }

    private void relationsToString(StringBuilder sb) {
        if (relations.isEmpty()) {
            return;
        }
        sb.append(StringUtils.repeat(" ", 3)).append("Relations:\n");
        relations.stream().forEach(
                relation -> sb.append(StringUtils.repeat(" ", 3)).append(" - ").append(relation.toString()).append("\n")
        );
    }

    private void fieldsToString(StringBuilder sb) {
        if (fields.isEmpty()) {
            return;
        }
        sb.append(StringUtils.repeat(" ", 3)).append("Fields:\n");
        fields.keySet().stream().forEach(
                key -> sb.append(StringUtils.repeat(" ", 3)).append(" - ").append(fields.get(key).toString()).append("\n")
        );
    }


}
