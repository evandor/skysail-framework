package io.skysail.server.model;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import io.skysail.domain.*;
import io.skysail.server.restlet.resources.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@ToString
@Getter
@Slf4j
public class TreeStructure {

    private String name;
    private String headline;
    private String link = "/";
    private String glyph;
    private List<TreeStructure> subfolders = new ArrayList<>();

    public static List<TreeStructure> from(@NotNull SkysailServerResource<?> resource) {
        List<TreeStructure> result = new ArrayList<>();
        return resource.getTreeRepresentation();
//        result.addAll(rootNodes.stream().map(rootNode -> new TreeStructure(rootNode, resource)).collect(Collectors.toList()));
//        return result;
    }
    
    public TreeStructure(@NonNull Nameable nameable, String link, String glyph) {
        this.name = nameable.getName();
        this.headline = nameable.getClass().getSimpleName();
        this.glyph = glyph;
        List<Field> collectionsFields = Arrays.stream(nameable.getClass().getDeclaredFields())
            .filter(field -> Collection.class.isAssignableFrom(field.getType()))
            .collect(Collectors.toList());

        if (!collectionsFields.isEmpty()) {
            this.link = link + "/" + ((Identifiable)nameable).getId().replace("#", "") + "/";
            this.link += collectionsFields.get(0).getName();
        }
        
        collectionsFields.stream().forEach(collectionField -> {
            try {
                collectionField.setAccessible(true);
                Collection<?> collection = (Collection<?>) collectionField.get(nameable);
                List<Nameable> subs = collection.stream()
                        .filter(e -> e instanceof Nameable)
                        .map(Nameable.class::cast).collect(Collectors.toList());
                subs.stream().forEach(subFolder -> 
                    addFolder(new TreeStructure(subFolder, this.link, glyph.equals("list-alt") ? "chevron-right" : "list-alt"))
                );
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });
    }
    
    private void addFolder(TreeStructure treeRepresentation) {
        subfolders.add(treeRepresentation);
    }

    private TreeStructure(TreeNode treeNode, SkysailServerResource<?> resource) {
        this.name = treeNode.getName();
        this.headline = treeNode.getHeadline();
        this.glyph = treeNode.getGlyph();
        List<String> baseRef = resource.getOriginalRef().getSegments();
        this.link = "/" + baseRef.get(0) + "/" + baseRef.get(1) + "/" + baseRef.get(2) + treeNode.getLink();
        treeNode.getSubNodes().stream().forEach(subNodes -> 
            subfolders.add(new TreeStructure(subNodes, resource))
        );
    }

}
