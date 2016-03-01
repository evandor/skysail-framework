package io.skysail.server.restlet.resources;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import io.skysail.domain.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@ToString
@Slf4j
public class TreeNode {

    private String name;
    private String headline;
    private String link = "#";
    private String glyph = "leaf";
    private List<TreeNode> subNodes = new ArrayList<>();

    public TreeNode(@NonNull Nameable nameable, String link, String glyph) {
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
                    addFolder(new TreeNode(subFolder, this.link, glyph.equals("list-alt") ? "chevron-right" : "list-alt"))
                );
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });
    }

    public void addFolder(TreeNode treeRepresentation) {
        subNodes.add(treeRepresentation);
    }

}
