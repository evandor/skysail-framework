package io.skysail.domain.core;

import lombok.*;

@Getter
@AllArgsConstructor
public class EntityRelation {

    private final String name;
    private final EntityModel targetEntityModel;
    private final EntityRelationType type;

    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName()).append("(");
        sb.append("name=").append(name).append(", ");
        sb.append("targetEntityModel=").append(targetEntityModel != null ? targetEntityModel.getSimpleName():"null").append(", ");
        sb.append("type=").append(type.name()).append(")");
        return sb.toString();
    }
}
