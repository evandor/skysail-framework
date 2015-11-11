package io.skysail.server.codegen.apt.processors.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.server.codegen.annotations.GenerateResources;
import io.skysail.server.codegen.apt.processors.*;

import java.util.*;

import javax.annotation.processing.*;
import javax.lang.model.element.*;

import org.junit.*;
import org.mockito.Mockito;

public class EntityProcessorTest {

    private Processors processor;
    private RoundEnvironment roundEnv;
    private ProcessingEnvironment processingEnv;
    private Set<TypeElement> elements;

    @Before
    public void setUp() throws Exception {
        processingEnv = Mockito.mock(ProcessingEnvironment.class);
        processor = new EntityProcessor();
        processor.init(processingEnv);
        roundEnv = Mockito.mock(RoundEnvironment.class);

        Messager messager = Mockito.mock(Messager.class);
        Mockito.when(processingEnv.getMessager()).thenReturn(messager);
        //Mockito.when(messager.printMessage(javax.tools.Diagnostic.Kind.NOTE, "").
    }

    @Test
    public void no_annotations_found_yields_successful_processing() {
        assertThat(processor.process(new HashSet<>(), roundEnv),is(true));
    }

    @Test
    public void one_annotation_found_yields_successful_processing() {
        elements = new HashSet<>();
        TypeElement element = Mockito.mock(TypeElement.class);
        elements.add(element);

        javax.lang.model.element.Name simpleName = Mockito.mock(javax.lang.model.element.Name.class);
        Mockito.when(element.getSimpleName()).thenReturn(simpleName);
        Element enclosingElement = Mockito.mock(Element.class);
        Mockito.when(element.getEnclosingElement()).thenReturn(enclosingElement);
        Mockito.doReturn(elements).when(roundEnv).getElementsAnnotatedWith(GenerateResources.class);

        boolean processed = processor.process(elements, roundEnv);

        assertThat(processed,is(true));
    }
}
