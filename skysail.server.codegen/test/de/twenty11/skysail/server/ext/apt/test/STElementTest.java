package de.twenty11.skysail.server.ext.apt.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.server.ext.apt.model.entities.Entity;
import io.skysail.server.ext.apt.model.entities.EntityGraph;
import io.skysail.server.ext.apt.model.entities.Reference;

import java.lang.annotation.Annotation;
import java.util.HashSet;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import de.twenty11.skysail.server.ext.apt.STElement;
import de.twenty11.skysail.server.ext.apt.annotations.GenerateEntityResource;
import de.twenty11.skysail.server.ext.apt.annotations.GenerateListResource;
import de.twenty11.skysail.server.ext.apt.annotations.GeneratePostResource;
import de.twenty11.skysail.server.ext.apt.annotations.GeneratePutResource;

public class STElementTest {

    private HashSet<Entity> nodes;
    private HashSet<Reference> edges;
    private Entity type1, type2;
    private Element element;
    private EntityGraph graph;
    private Name name;

    @Before
    public void setUp() throws Exception {
        type1 = new Entity("io.skysail", "Type1");
        type2 = new Entity("io.skysail", "Type2");
        nodes = new HashSet<>();
        edges = new HashSet<>();
        nodes.add(type1);
        nodes.add(type2);
        element = Mockito.mock(Element.class);
        name = Mockito.mock(Name.class);
        Mockito.when(element.getSimpleName()).thenReturn(name);
        Element enclosingElement = Mockito.mock(Element.class);
        Mockito.when(enclosingElement.toString()).thenReturn("io.skysail");
        Mockito.when(element.getEnclosingElement()).thenReturn(enclosingElement);

        graph = new EntityGraph(nodes, edges);
    }

    @Test
    public void listResourcePath_for_type1_is_calculated_correctly() throws Exception {
        Mockito.when(name.toString()).thenReturn("Type1");
        Class<? extends Annotation> annotation = GenerateListResource.class;
        edges.add(new Reference(type1, type2));
        STElement stElement = new STElement(element, graph, annotation);
        assertThat(stElement.getPath(), is(equalTo("/Type1s")));
    }

    @Test
    public void listResourcePath_for_type2_is_calculated_correctly() throws Exception {
        Mockito.when(name.toString()).thenReturn("Type2");
        Class<? extends Annotation> annotation = GenerateListResource.class;
        edges.add(new Reference(type1, type2));
        STElement stElement = new STElement(element, graph, annotation);
        assertThat(stElement.getPath(), is(equalTo("/Type1s/{id}/Type2s")));
    }
    
    @Test
    public void entityResourcePath_for_type1_is_calculated_correctly() throws Exception {
        Mockito.when(name.toString()).thenReturn("Type1");
        Class<? extends Annotation> annotation = GenerateEntityResource.class;
        edges.add(new Reference(type1, type2));
        STElement stElement = new STElement(element, graph, annotation);
        assertThat(stElement.getPath(), is(equalTo("/Type1s/{id}")));
    }

    @Test
    public void entityResourcePath_for_type2_is_calculated_correctly() throws Exception {
        Mockito.when(name.toString()).thenReturn("Type2");
        Class<? extends Annotation> annotation = GenerateEntityResource.class;
        edges.add(new Reference(type1, type2));
        STElement stElement = new STElement(element, graph, annotation);
        assertThat(stElement.getPath(), is(equalTo("/Type2s/{id}")));
    }
    
    @Test
    public void postEntityResourcePath_for_type1_is_calculated_correctly() throws Exception {
        Mockito.when(name.toString()).thenReturn("Type1");
        Class<? extends Annotation> annotation = GeneratePostResource.class;
        edges.add(new Reference(type1, type2));
        STElement stElement = new STElement(element, graph, annotation);
        assertThat(stElement.getPath(), is(equalTo("/Type1s/")));
    }

    @Test
    public void postEentityResourcePath_for_type2_is_calculated_correctly() throws Exception {
        Mockito.when(name.toString()).thenReturn("Type2");
        Class<? extends Annotation> annotation = GeneratePostResource.class;
        edges.add(new Reference(type1, type2));
        STElement stElement = new STElement(element, graph, annotation);
        assertThat(stElement.getPath(), is(equalTo("/Type1s/{id}/Type2s/")));
    }
    
    @Test
    public void putEntityResourcePath_for_type1_is_calculated_correctly() throws Exception {
        Mockito.when(name.toString()).thenReturn("Type1");
        Class<? extends Annotation> annotation = GeneratePutResource.class;
        edges.add(new Reference(type1, type2));
        STElement stElement = new STElement(element, graph, annotation);
        assertThat(stElement.getPath(), is(equalTo("/Type1s/{id}/")));
    }

    @Test
    @Ignore
    public void putEentityResourcePath_for_type2_is_calculated_correctly() throws Exception {
        Mockito.when(name.toString()).thenReturn("Type2");
        Class<? extends Annotation> annotation = GeneratePutResource.class;
        edges.add(new Reference(type1, type2));
        STElement stElement = new STElement(element, graph, annotation);
        assertThat(stElement.getPath(), is(equalTo("/Type1s/{Type1Id}/Type2s/")));
    }
}
