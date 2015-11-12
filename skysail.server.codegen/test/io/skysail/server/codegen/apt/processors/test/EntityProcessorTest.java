package io.skysail.server.codegen.apt.processors.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import io.skysail.server.codegen.annotations.GenerateResources;
import io.skysail.server.codegen.apt.processors.*;

import java.util.*;

import javax.annotation.processing.*;
import javax.lang.model.element.*;

import org.junit.*;

public class EntityProcessorTest {

    private Processors processor;
    private RoundEnvironment roundEnv;
    private ProcessingEnvironment processingEnv;
    private Set<TypeElement> elements;

    @Before
    public void setUp() throws Exception {
        processingEnv = mock(ProcessingEnvironment.class);
        processor = new EntityProcessor();
        processor.init(processingEnv);
        roundEnv = mock(RoundEnvironment.class);

        Messager messager = mock(Messager.class);
        when(processingEnv.getMessager()).thenReturn(messager);
        //when(messager.printMessage(javax.tools.Diagnostic.Kind.NOTE, "").
    }

    @Test
    public void no_annotations_found_yields_successful_processing() {
        assertThat(processor.process(new HashSet<>(), roundEnv),is(true));
    }

    @Test
    public void one_annotation_found_yields_successful_processing() {
        elements = new HashSet<>();
        TypeElement element = mock(TypeElement.class);
        elements.add(element);

        javax.lang.model.element.Name simpleName = mock(javax.lang.model.element.Name.class);
        when(element.getSimpleName()).thenReturn(simpleName);
        Element enclosingElement = mock(Element.class);
        when(element.getEnclosingElement()).thenReturn(enclosingElement);
        GenerateResources value = mock(GenerateResources.class);
        when(element.getAnnotation(GenerateResources.class)).thenReturn(value);
        when(value.application()).thenReturn("application");
        doReturn(elements).when(roundEnv).getElementsAnnotatedWith(GenerateResources.class);

        boolean processed = processor.process(elements, roundEnv);

        assertThat(processed,is(true));
    }
}
