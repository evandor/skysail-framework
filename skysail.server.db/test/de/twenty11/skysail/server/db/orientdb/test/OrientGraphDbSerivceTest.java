//package de.twenty11.skysail.server.db.orientdb.test;
//
//import java.util.Dictionary;
//import java.util.Hashtable;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import com.tinkerpop.blueprints.Edge;
//import com.tinkerpop.blueprints.Vertex;
//import com.tinkerpop.blueprints.impls.orient.OrientGraph;
//
//import de.twenty11.skysail.server.db.orientdb.OrientGraphDbSerivce;
//
//public class OrientGraphDbSerivceTest {
//
//	private OrientGraphDbSerivce dbSerivce;
//
//	@Before
//	public void setUp() throws Exception {
//		dbSerivce = new OrientGraphDbSerivce();
//		Dictionary<String, ?> properties = new Hashtable<>();
//		dbSerivce.updated(properties);
//	}
//
//	@Test
//	public void testName() throws Exception {
//		OrientGraph graph = dbSerivce.getGraph();
//		Vertex luca = graph.addVertex(null); 
//		luca.setProperty("name", "Luca");
//		Vertex marko = graph.addVertex(null);
//		marko.setProperty("name", "Marko");
//		Edge lucaKnowsMarko = graph.addEdge(null, luca, marko, "knows");
//		graph.commit();
//	}
//}
