package io.skysail.domain.core;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
    
    public String getSetterMethodName () {
        return "set" + name.substring(0,1).toUpperCase() + name.substring(1);
    }
    
    public String getGetterMethodName () {
        return "get" + name.substring(0,1).toUpperCase() + name.substring(1);
    }
}
