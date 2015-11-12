package io.skysail.server.codegen.model.entities;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EntityGraph {

    private Set<AptEntity> nodes;
    private Set<Reference> edges;

    public EntityGraph(Set<AptEntity> nodes, Set<Reference> edges) {
        this.nodes = nodes;
        this.edges = edges;
    }

    public List<AptEntity> getNodesWithNoIncomingEdge() {
        return nodes.stream().filter(node -> {
            return !edgePointingToCurrentNodeExists(node);
        }).collect(Collectors.toList());
    }

    private boolean edgePointingToCurrentNodeExists(AptEntity n) {
        return edges.stream().filter(e -> {
            return e.getTo().equals(n);
        }).findFirst().isPresent();
    }

    public List<Reference> getIncomingEdges(AptEntity n) {
        return edges.stream().filter(edge -> {
            return edge.getTo().equals(n);
        }).collect(Collectors.toList());
    }

    public List<Reference> getOutgoingEdges(AptEntity entity) {
        return edges.stream().filter(edge -> {
            return edge.getFrom().equals(entity);
        }).collect(Collectors.toList());
    }

    public AptEntity getNode(String packageName, String simpleName) {
        return nodes.stream().filter(n -> {
            return n.getSimpleName().equals(simpleName) && n.getPackageName().equals(packageName);
        }).findFirst().orElse(null);
    }

    public Set<AptEntity> getNodes() {
        return Collections.unmodifiableSet(nodes);
    }


}
