package io.skysail.server.restlet.resources.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.*;

import org.junit.*;

import io.skysail.domain.Nameable;
import io.skysail.server.restlet.resources.TreeRepresentation;
import lombok.*;

public class TreeRepresentationTest {

    private NameableRootEntity root;


    @Getter
    @Setter
    private class NameableRootEntity implements Nameable {
        private String name,id = "theId";
        private List<NameableSubEntity> subEntities =  new ArrayList<>();
        public NameableRootEntity(String name) {
            this.name = name;
        }
    }

    @Getter
    private class NameableSubEntity implements Nameable {
        @Setter
        private String id = "theId";
        private String name;
        public NameableSubEntity(String name) {
            this.name = name;
        }
    }

    @Before
    public void setUp() throws Exception {
        root = new NameableRootEntity("root");
    }

    @Test
    public void simpleEntity_is_processed_correctly() {
        TreeRepresentation treeRepresentation = new TreeRepresentation(root,"");
        assertThat(treeRepresentation.getName(),is("root"));
        assertThat(treeRepresentation.getSubfolders().size(),is(0));
    }
    
    @Test
    public void entity_with_subEntity_is_processed_correctly() {
        List<NameableSubEntity> subEntities = new ArrayList<>();
        subEntities.add(new NameableSubEntity("sub1"));
        subEntities.add(new NameableSubEntity("sub2"));
        root.setSubEntities(subEntities);
        
        TreeRepresentation treeRepresentation = new TreeRepresentation(root,"");
        assertThat(treeRepresentation.getName(),is("root"));
        assertThat(treeRepresentation.getSubfolders().size(),is(2));
        assertThat(treeRepresentation.getSubfolders().get(0).getName(),is("sub1"));
        assertThat(treeRepresentation.getSubfolders().get(1).getName(),is("sub2"));
    }
}
