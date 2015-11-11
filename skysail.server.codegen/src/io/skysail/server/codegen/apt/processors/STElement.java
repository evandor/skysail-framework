package io.skysail.server.codegen.apt.processors;

import io.skysail.server.codegen.model.entities.*;
import io.skysail.server.ext.apt.model.entities.*;

import java.lang.annotation.Annotation;

import javax.lang.model.element.Element;

import lombok.Getter;

@Getter
public class STElement {

	private EntityGraph graph;
	private String simpleName;
	private String enclosingElement;
	private Class<? extends Annotation> gpr;

	public STElement(Element element, EntityGraph graph, Class<? extends Annotation> gpr) {
		this.gpr = gpr;
		this.simpleName = element.getSimpleName().toString();
		this.enclosingElement = element.getEnclosingElement().toString();
		this.graph = graph;
	}

	public String getPath() {
		Entity entity = new Entity(enclosingElement.toString(), simpleName);
		String path = getPathTemplate(simpleName);
		if (graph.getNodesWithNoIncomingEdge().contains(entity)) {
			return path;
		} else {
			Reference firstIncomingEdge = graph.getIncomingEdges(entity).get(0);
			Entity from = firstIncomingEdge.getFrom();
			return "/" + from.getSimpleName() + "s/{id}" + path;
		}
	}

	public String getVariableName() {
		return simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1);
	}

	private String getPathTemplate(String simpleName) {
//		if (gpr.isAssignableFrom(GeneratePostResource.class)) {
//			return "/" + simpleName + "s/";
//		}
////		if (gpr.isAssignableFrom(GenerateEntityResource.class)) {
////            return "/" + simpleName + "s/{id}";
////        }
//        if (gpr.isAssignableFrom(GeneratePutResource.class)) {
//            return "/" + simpleName + "s/{id}/";
//        }
		return "/" + simpleName + "s";
	}

}
