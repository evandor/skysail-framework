package io.skysail.server.ext.apt.model.types;

import io.skysail.server.ext.apt.model.entities.Entity;
import io.skysail.server.ext.apt.model.entities.EntityGraph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Element;

import lombok.Getter;

/**
 * A TypeModel provides all the information to actually create the java files
 * derived from the annotated model classes.
 * 
 * It is instantiated with an EntiyGraph model which provides the entities and
 * relations between them.
 *
 */
@Getter
public class TypeModel {

	private EntityGraph graph;
	private ApplicationModel application;
	private RootResourceModel rootResource;
	private List<RepositoryModel> repos;
	private List<ResourceModel> resources;

	public TypeModel(EntityGraph graph, Element skysailApplicationElement) {
		this.graph = graph;
		this.application = new ApplicationModel(skysailApplicationElement);
		this.rootResource = new RootResourceModel(skysailApplicationElement);
		this.repos = createRepos();
		this.resources = createResources();
	}

	private List<ResourceModel> createResources() {
		return Collections.emptyList();
	}

	private List<RepositoryModel> createRepos() {
	    List<RepositoryModel> repos = new ArrayList<>();
	    Set<Entity> rootEntities = graph.getNodes();//WithNoIncomingEdge();
	    for (Entity entity : rootEntities) {
            repos.add(new RepositoryModel(entity, graph));
        }
		return repos;
	}

}
