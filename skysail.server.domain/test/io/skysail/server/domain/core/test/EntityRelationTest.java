package io.skysail.server.domain.core.test;

import org.junit.*;

import io.skysail.server.domain.core.EntityRelation;

public class EntityRelationTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testName() {
        EntiyModel entityModel;
        new EntityRelation(entityModel, EntityRelation.TYPE.ONE_TO_MANY);
    }
}
