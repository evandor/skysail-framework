package io.skysail.server.db.impl.test;

import io.skysail.server.db.impl.Updater;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.tinkerpop.blueprints.impls.orient.*;

@RunWith(MockitoJUnitRunner.class)
public class UpdaterTest {

    private Updater updater;
    private OrientGraph db;
    private Goal entity;

    @Before
    public void setUp() throws Exception {
        db = Mockito.mock(OrientGraph.class);
        updater = new Updater(db, new String[]{"achievements"});
        entity = new Goal();
        entity.setId("goalId");
        OrientVertex goalVertex = Mockito.mock(OrientVertex.class);
        Mockito.when(db.getVertex("goalId")).thenReturn(goalVertex);
    }

    @Test
    public void testUpdate() throws Exception {
        Object updated = updater.update(entity);
        System.out.println(updated);
    }

}
