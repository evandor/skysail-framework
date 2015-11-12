package io.skysail.server.codegen.model.types;

import io.skysail.server.codegen.model.entities.*;

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
public class TypeModel {

	private EntityGraph graph;
	private JavaApplication application;
	private RootResourceModel rootResource;
	private List<RepositoryModel> repos;
	private List<ResourceModel> resources;

	public TypeModel(EntityGraph graph) {
		this.graph = graph;
		this.application = new JavaApplication("");
		this.rootResource = new RootResourceModel();
		this.repos = createRepos();
		this.resources = createResources();
	}

	private List<ResourceModel> createResources() {
		return Collections.emptyList();
	}

	private List<RepositoryModel> createRepos() {
	    List<RepositoryModel> repos = new ArrayList<>();
	    Set<AptEntity> rootEntities = graph.getNodes();
	    for (AptEntity entity : rootEntities) {
            repos.add(new RepositoryModel(entity, graph));
        }
		return repos;
	}

}
