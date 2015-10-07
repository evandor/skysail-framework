package de.twenty11.skysail.server.db.orientdb.impl.test;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.tinkerpop.blueprints.impls.orient.OrientGraph;

import de.twenty11.skysail.server.db.orientdb.impl.Updater;

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
    }

    @Test
    public void testUpdate() throws Exception {
        updater.update(entity);
    }

}
