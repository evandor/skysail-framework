//package io.skysail.server.ext.apt.model.types.test;
//
//import static org.hamcrest.Matchers.containsString;
//import static org.junit.Assert.assertThat;
//import io.skysail.server.ext.apt.model.entities.*;
//import io.skysail.server.ext.apt.model.types.*;
//
//import java.util.*;
//
//import javax.lang.model.element.*;
//
//import org.junit.*;
//import org.junit.runner.RunWith;
//import org.mockito.Mockito;
//import org.mockito.runners.MockitoJUnitRunner;
//
//@RunWith(MockitoJUnitRunner.class)
//@Ignore
//public class TypeModelTest {
//
//	private EntityGraph graph;
//
//	private TypeModel typeModel;
//
//	private Element skysailApplicationElement;
//
//	@Before
//	public void setUp() throws Exception {
//		Set<AptEntity> nodes = new HashSet<>();
//		Set<Reference> edges = new HashSet<>();
//		graph = new EntityGraph(nodes, edges);
//		skysailApplicationElement = Mockito.mock(Element.class);
//		Element enclosingElement = Mockito.mock(Element.class);
//		Mockito.when(enclosingElement.toString()).thenReturn("io.skysail.server.test.crm");
//		Mockito.when(skysailApplicationElement.getEnclosingElement()).thenReturn(enclosingElement);
//		Name name = Mockito.mock(Name.class);
//		Mockito.when(name.toString()).thenReturn("CrmApplication");
//		Mockito.when(skysailApplicationElement.getSimpleName()).thenReturn(name);
//	}
//
//	@Test
//	public void testTypeModel() throws Exception {
//		typeModel = new TypeModel(graph, skysailApplicationElement);
//		ApplicationModel application = typeModel.getApplication();
//		assertThat(application.toString(), containsString("package io.skysail.server.test.crm;"));
//		assertThat(application.toString(), containsString("import de.twenty11.skysail.server.app.SkysailApplication;"));
//		assertThat(application.toString(), containsString("import de.twenty11.skysail.server.app.ApplicationProvider;"));
//		assertThat(application.toString(),
//		        containsString("import de.twenty11.skysail.server.services.MenuItemProvider;"));
//	}
//
//}
