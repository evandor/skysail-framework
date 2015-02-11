package io.skysail.server.ext.apt.model.types.test;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;

import org.junit.Before;
import org.mockito.Mockito;

public class ModelTestBase {

    protected Element element;
    protected Name name;

    @Before
    public void setUp() throws Exception {
        element = Mockito.mock(Element.class);
        name = Mockito.mock(Name.class);
        Mockito.when(element.getSimpleName()).thenReturn(name);
        Element enclosingElement = Mockito.mock(Element.class);
        Mockito.when(element.getEnclosingElement()).thenReturn(enclosingElement);
        Mockito.when(enclosingElement.toString()).thenReturn("io.skysail.server.test.crm");
    }

}
