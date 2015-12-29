package io.skysail.server.model;

import java.util.*;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import io.skysail.server.restlet.resources.*;
import lombok.*;

@NoArgsConstructor
@ToString
public class TreeStructure {

    @Getter
    private String name;
    
    @Getter
    private String headline;

    @Getter
    private String link = "/";

    @Getter
    private List<TreeStructure> subfolders = new ArrayList<>();

    public TreeStructure(TreeRepresentation treeRepresentation, SkysailServerResource<?> resource) {
        this.name = treeRepresentation.getName();
        this.headline = treeRepresentation.getHeadline();
        List<String> baseRef = resource.getReference().getSegments();
        this.link = "/" + baseRef.get(0) + "/" + baseRef.get(1) + "/" + baseRef.get(2) + treeRepresentation.getLink();
        treeRepresentation.getSubfolders().stream().forEach(subfolder -> 
            subfolders.add(new TreeStructure(subfolder, resource))
        );
    }

    public static List<TreeStructure> from(@NotNull SkysailServerResource<?> resource) {
        List<TreeStructure> result = new ArrayList<>();
        List<TreeRepresentation> treeRepresentation = resource.getTreeRepresentation();
        result.addAll(treeRepresentation.stream().map(tR -> new TreeStructure(tR, resource)).collect(Collectors.toList()));
        return result;
    }
}
