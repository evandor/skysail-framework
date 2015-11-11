package io.skysail.server.codegen.model.types;

import io.skysail.server.codegen.model.entities.*;
import io.skysail.server.ext.apt.model.entities.*;

import java.util.*;

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
public class TypeModel2 {

	private EntityGraph graph;
	private ApplicationModel2 application;
	private RootResourceModel2 rootResource;
	private List<RepositoryModel> repos;
	private List<ResourceModel> resources;

	public TypeModel2(EntityGraph graph) {
		this.graph = graph;
		this.application = new ApplicationModel2();
		this.rootResource = new RootResourceModel2();
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
