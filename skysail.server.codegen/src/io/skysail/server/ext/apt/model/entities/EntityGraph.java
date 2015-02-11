package io.skysail.server.ext.apt.model.entities;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EntityGraph {

    private Set<Entity> nodes;
    private Set<Reference> edges;

    public EntityGraph(Set<Entity> nodes, Set<Reference> edges) {
        this.nodes = nodes;
        this.edges = edges;
    }

    public List<Entity> getNodesWithNoIncomingEdge() {
        return nodes.stream().filter(node -> {
            return !edgePointingToCurrentNodeExists(node);
        }).collect(Collectors.toList());
    }

    private boolean edgePointingToCurrentNodeExists(Entity n) {
        return edges.stream().filter(e -> {
            return e.getTo().equals(n);
        }).findFirst().isPresent();
    }

    public List<Reference> getIncomingEdges(Entity n) {
        return edges.stream().filter(edge -> {
            return edge.getTo().equals(n);
        }).collect(Collectors.toList());
    }

    public List<Reference> getOutgoingEdges(Entity entity) {
        return edges.stream().filter(edge -> {
            return edge.getFrom().equals(entity);
        }).collect(Collectors.toList());
    }

    public Entity getNode(String packageName, String simpleName) {
        return nodes.stream().filter(n -> {
            return n.getSimpleName().equals(simpleName) && n.getPackageName().equals(packageName);
        }).findFirst().orElse(null);
    }

    public Set<Entity> getNodes() {
        return Collections.unmodifiableSet(nodes);
    }


}
