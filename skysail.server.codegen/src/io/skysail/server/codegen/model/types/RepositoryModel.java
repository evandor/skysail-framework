package io.skysail.server.codegen.model.types;

import java.util.List;
import java.util.stream.Collectors;

import io.skysail.server.codegen.model.entities.*;

public class RepositoryModel extends JavaModel {

    private List<Reference> outgoingEdges;

    public RepositoryModel(AptEntity entity, EntityGraph graph) {
        typeName = entity.getSimpleName() + "sRepository";
        entityName = entity.getSimpleName();
        packageName = entity.getPackageName();// + "." +
                                              // firstLowercase(entity.getSimpleName())+"s";
        outgoingEdges = graph.getOutgoingEdges(entity);
        String importsFromReferencedClasses = outgoingEdges.stream().map(ref -> {return "import " + ref.getTo().getPackageName() +".*;\n";}).collect(Collectors.joining());
        imports.add(importsFromReferencedClasses);
    }

    public String getLinkedClasses() {
        String linkedClasses = outgoingEdges.stream().map(ref -> {
            return ref.getTo().getSimpleName() + ".class";
        }).collect(Collectors.joining(", "));
        return linkedClasses == null || "".equals(linkedClasses.trim()) ? "" : ", " + linkedClasses;
    }

}
