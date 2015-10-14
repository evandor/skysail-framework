package de.twenty11.skysail.server.ext.apt.processors.test;

import java.util.*;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;

import org.junit.Test;
import org.mockito.Mockito;

import de.twenty11.skysail.server.ext.apt.*;
import de.twenty11.skysail.server.ext.apt.processors.ApplicationProcessor;

public class ApplicationProcessorTest {

	@Test
	public void testName() throws Exception {
		Processors processor = new ApplicationProcessor();
		Set<TypeElement> annotations = new HashSet<>();
		TypeElement annotation = Mockito.mock(TypeElement.class);
		annotations.add(annotation);
		RoundEnvironment roundEnv = Mockito.mock(RoundEnvironment.class);
		Set<TypeElement> entities = new HashSet<TypeElement>();

		Set<Element> asList = new HashSet<>();
		TestElement testElement = new TestElement();
		asList.add(testElement);
		// Mockito.mock(com.sun.java.class);
		// ExecutableElement element = makeDummyTestMethod(testClass);
		// Set<? extends Element> asList;
		// entities.add(element);
		// List<? extends Element> asList = Arrays.asList(element);
		// Mockito.doReturn(asList).when(roundEnv).getElementsAnnotatedWith(GenerateEntityResource.class);
		boolean process = processor.process(annotations, roundEnv);
	}

	private ExecutableElement makeDummyTestMethod(Element testClass) {
		Name dummyName = Mockito.mock(Name.class);
		Mockito.when(dummyName.toString()).thenReturn("TestMethod");

		ExecutableElement testMethod = Mockito.mock(ExecutableElement.class);
		Mockito.when(testMethod.getEnclosingElement()).thenReturn(testClass);
		Mockito.when(testMethod.getSimpleName()).thenReturn(dummyName);

		// def answer = { invocation ->
		// def suite = invocation.arguments[1];
		// def mock = invocation.mock;
		//
		// processor.visitor.visitExecutableAsMethod(mock, suite)
		// } as Answer<Void>
		// when testMethod.accept(any(), any()) thenAnswer answer

		// def testAnnotation = Mockito.mock(AcceptanceTest.class);
		// when testAnnotation.id() thenReturn "1.1"
		// when testMethod.getAnnotation(AcceptanceTest) thenReturn
		// testAnnotation

		return testMethod;
	}

}
