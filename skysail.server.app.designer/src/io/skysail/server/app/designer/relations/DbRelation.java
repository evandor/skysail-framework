package io.skysail.server.app.designer.relations;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DbRelation implements Identifiable, Serializable {

    private static final long serialVersionUID = 6261172753990498181L;

    private String id;
    
    @Field
    @NotNull
    @Size(min=2)
    private String name;
    
    @Field(selectionProvider = RelationTargetSelectionProvider.class)
    private String target;
    
    @Field(selectionProvider = RelationTypeSelectionProvider.class)
    private String relationType;

}
