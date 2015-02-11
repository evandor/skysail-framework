package io.skysail.server.ext.apt.model.entities.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import io.skysail.server.ext.apt.model.entities.Entity;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EntityTest {

    private Entity entity;

    @Before
    public void setUp() {
        entity = new Entity("packageName", "simpleName");
    }

    @Test
    public void testProperties() throws Exception {
        assertThat(entity.getPackageName(), is(equalTo("packageName")));
        assertThat(entity.getSimpleName(), is(equalTo("simpleName")));
    }

    @Test
    public void two_entities_are_equal_if_they_have_the_same_package_and_simpleName() {
        Entity entity2 = new Entity("packageName", "simpleName");
        assertThat(entity, is(equalTo(entity2)));
        assertThat(entity.hashCode(), is(equalTo(entity2.hashCode())));
    }

    @Test
    public void two_entities_are_not_equal_if_they_have_different_packages_or_simpleNames() {
        Entity entity2 = new Entity("packageName", "simpleName2");
        assertThat(entity, is(not(equalTo(entity2))));
        Entity entity3 = new Entity("other", "simpleName");
        assertThat(entity, is(not(equalTo(entity3))));
    }

    @Test
    public void constructor_accepts_element() {
        Element element = Mockito.mock(Element.class);

        Name name = Mockito.mock(Name.class);
        Mockito.when(name.toString()).thenReturn("simpleName");
        Mockito.when(element.getSimpleName()).thenReturn(name);
        Element enclosingElement = Mockito.mock(Element.class);
        Mockito.when(enclosingElement.toString()).thenReturn("packageName");
        Mockito.when(element.getEnclosingElement()).thenReturn(enclosingElement);

        Entity entity = new Entity(element);
        assertThat(entity.getSimpleName(), is(equalTo("simpleName")));
        assertThat(entity.getPackageName(), is(equalTo("packageName")));
    }

    @Test
    public void toString_contains_Package_and_simpleName() {
        assertThat(entity.toString(), is(equalTo("Entity(packageName, simpleName)")));
    }

    @Test
    public void getElementName_is_null() {
        assertThat(entity.getElementName(), is(nullValue()));
    }
}
