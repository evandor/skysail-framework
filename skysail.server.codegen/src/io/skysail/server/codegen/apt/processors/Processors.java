package io.skysail.server.codegen.apt.processors;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.*;

import javax.annotation.processing.*;
import javax.lang.model.element.*;
import javax.tools.JavaFileObject;

public abstract class Processors extends AbstractProcessor {

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

	protected Set<? extends Element> getElements(RoundEnvironment roundEnv, Class<? extends Annotation> gpr) {
		return roundEnv.getElementsAnnotatedWith(gpr);
	}

	protected Set<? extends Element> getSubElements(RoundEnvironment roundEnv, Class<? extends Annotation> gpr,
	        Element skysailApplicationElement) {
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
