package io.skysail.domain.core;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
public class EntityRelation {

    private final String name;
    private final EntityModel targetEntityModel;
    private final EntityRelationType type;


}
