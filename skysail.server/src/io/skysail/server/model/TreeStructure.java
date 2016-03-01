package io.skysail.server.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.restlet.resources.TreeRepresentation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@ToString
@Getter
public class TreeStructure {

    private String name;
    private String headline;
    private String link = "/";
    private List<TreeStructure> subfolders = new ArrayList<>();
    private String glyph;

    public TreeStructure(TreeRepresentation treeRepresentation, SkysailServerResource<?> resource) {
        this.name = treeRepresentation.getName();
        this.headline = treeRepresentation.getHeadline();
        this.glyph = treeRepresentation.getGlyph();
        List<String> baseRef = resource.getOriginalRef().getSegments();
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
