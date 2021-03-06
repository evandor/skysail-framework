package io.skysail.domain.core.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import io.skysail.domain.core.*;

public class EntityRelationTest {

    @Test
    public void defaultConstructor_initializes_target_and_type() {
        EntityModel entityModel = new EntityModel("theEntity");
        EntityRelation entityRelation = new EntityRelation("name", entityModel, EntityRelationType.ONE_TO_MANY);
        assertThat(entityRelation.getTargetEntityModel(),is(entityModel));
        assertThat(entityRelation.getType(),is(EntityRelationType.ONE_TO_MANY));
    }
}
