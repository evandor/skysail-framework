package io.skysail.server.db.impl.test;

import io.skysail.server.db.impl.Updater;

import java.util.*;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.tinkerpop.blueprints.*;
import com.tinkerpop.blueprints.impls.orient.*;

@RunWith(MockitoJUnitRunner.class)
public class UpdaterTest {

    private static final String COMMENT_ID = "#comment:Id";
    private static final String GOAL_ID = "#goal:Id";

    private OrientGraph db;
    private Goal goal;
    private OrientVertex goalVertex;
    private OrientVertex commentVertex;
    private Edge edge;

    @Before
    public void setUp() throws Exception {
        db = Mockito.mock(OrientGraph.class);
        goal = new Goal();
        goal.setId(GOAL_ID);
        goalVertex = Mockito.mock(OrientVertex.class);
        Mockito.when(db.getVertex(GOAL_ID)).thenReturn(goalVertex);
        commentVertex = Mockito.mock(OrientVertex.class);
        Mockito.when(db.getVertex(COMMENT_ID)).thenReturn(commentVertex);
        List<Edge> edgesIterable = new ArrayList<>();
        edge = Mockito.mock(Edge.class);
        edgesIterable.add(edge);
        Mockito.when(goalVertex.getEdges(Direction.OUT, "comment")).thenReturn(edgesIterable);
    }

    @Test
    public void updater_adds_single_edge_for_edgeType_string() throws Exception {
        goal.setComment(COMMENT_ID);
        new Updater(db, new String[]{"comment"}).update(goal);
        Mockito.verify(db).removeEdge(edge);
        Mockito.verify(db).addEdge(null, goalVertex, commentVertex, "comment");
    }

    @Test
    public void updater_adds_new_edge_for_edgeType_collection() throws Exception {
        List<Achievement> achievements = Arrays.asList(new Achievement());
        goal.setAchievements(achievements);
        new Updater(db, new String[]{"achievements"}).update(goal);
       // Mockito.verify(db).addEdge(null, goalVertex, commentVertex, "comment");
    }

}
