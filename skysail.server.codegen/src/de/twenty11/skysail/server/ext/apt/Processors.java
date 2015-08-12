package de.twenty11.skysail.server.ext.apt;

import io.skysail.server.ext.apt.model.entities.EntityGraph;
import io.skysail.server.ext.apt.model.types.TypeModel;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

import de.twenty11.skysail.server.ext.apt.annotations.SkysailApplication;

public abstract class Processors extends AbstractProcessor {

	protected static EntityGraph graph;

	protected static TypeModel typeModel;

	public abstract boolean doProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)
	        throws Exception;

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		if (annotations.isEmpty()) {
			return true;
		}

		try {
			return doProcess(annotations, roundEnv);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	protected Element getElement(RoundEnvironment roundEnv, Class<SkysailApplication> annotationClass) {
		Set<? extends Element> skysailApplications = roundEnv.getElementsAnnotatedWith(annotationClass);
		if (skysailApplications == null || skysailApplications.size() == 0) {
			processingEnv.getMessager().printMessage(javax.tools.Diagnostic.Kind.ERROR,
			        "could not generate PostResource as there was no @SkysailApplication annotation.");
			return null;
		}
		if (skysailApplications.size() > 1) {
			processingEnv.getMessager().printMessage(javax.tools.Diagnostic.Kind.WARNING,
			        "there was more than one @SkysailApplication annotation.");
			return skysailApplications.iterator().next();
		}

		return skysailApplications.iterator().next();
	}

	protected Set<? extends Element> getElements(RoundEnvironment roundEnv, Class<? extends Annotation> gpr) {
		return roundEnv.getElementsAnnotatedWith(gpr);
	}

	protected Set<? extends Element> getSubElements(RoundEnvironment roundEnv, Class<? extends Annotation> gpr,
	        Element skysailApplicationElement) {
	    // trying to work around https://bugs.openjdk.java.net/browse/JDK-8051443
		/*return roundEnv.getElementsAnnotatedWith(gpr).stream().filter(e -> {
			return entityIsInSubpackgeOfApplication(e, skysailApplicationElement);
		}).collect(Collectors.toSet());*/
	    Set<Element> result = new HashSet<>();
	    for (Element element : roundEnv.getElementsAnnotatedWith(gpr)) {
	        if (entityIsInSubpackgeOfApplication(element, skysailApplicationElement)) {
	            result.add(element);
	        }
	    }
	    return result;
	}

	private boolean entityIsInSubpackgeOfApplication(Element e, Element skysailApplicationElement) {
		String entityPackage = e.getEnclosingElement().toString();
		String appPackage = skysailApplicationElement.getEnclosingElement().toString();
		printMessage(" +++ " + entityPackage + "/" + appPackage + ": " + entityPackage.startsWith(appPackage));
		return entityPackage.startsWith(appPackage);
	}

	protected Set<STElement> getElements2(RoundEnvironment roundEnv, Class<? extends Annotation> gpr, EntityGraph graph) {
		Set<? extends Element> elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(gpr);
		printMessage("Here: " + elementsAnnotatedWith + " " + gpr);
		Set<STElement> result = new HashSet<>();
		for (Element element : elementsAnnotatedWith) {
			result.add(new STElement(element, graph, gpr));
		}
		return result;
	}

	protected void printMessage(String msg) {
		processingEnv.getMessager().printMessage(javax.tools.Diagnostic.Kind.NOTE, msg);
	}

	protected String getTypeName(Element e) {
		return e.getEnclosingElement().toString() + "." + e.getSimpleName();
	}

	protected JavaFileObject createSourceFile(String name) throws IOException {
		JavaFileObject createdSourceFile = processingEnv.getFiler().createSourceFile(name);
		printMessage("Created source file: " + createdSourceFile.toUri());
		return createdSourceFile;
	}

}