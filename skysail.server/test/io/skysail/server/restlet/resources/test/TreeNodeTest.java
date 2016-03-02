//package io.skysail.server.restlet.resources.test;
//
//import static org.hamcrest.CoreMatchers.is;
//import static org.junit.Assert.assertThat;
//
//import java.util.*;
//
//import org.junit.*;
//
//import io.skysail.domain.Nameable;
//import io.skysail.server.restlet.resources.TreeNode;
//import lombok.*;
//
//public class TreeNodeTest {
//
//    private NameableRootEntity root;
//
//
//    @Getter
//    @Setter
//    private class NameableRootEntity implements Nameable {
//        private String name,id = "theId";
//        private List<NameableSubEntity> subEntities =  new ArrayList<>();
//        public NameableRootEntity(String name) {
//            this.name = name;
//        }
//    }
//
//    @Getter
//    private class NameableSubEntity implements Nameable {
//        @Setter
//        private String id = "theId";
//        private String name;
//        public NameableSubEntity(String name) {
//            this.name = name;
//        }
//    }
//
//    @Before
//    public void setUp() throws Exception {
//        root = new NameableRootEntity("root");
//    }
//
//    @Test
//    public void simpleEntity_is_processed_correctly() {
//        TreeNode treeRepresentation = new TreeNode(root,"","leaf");
//        assertThat(treeRepresentation.getName(),is("root"));
//        assertThat(treeRepresentation.getSubNodes().size(),is(0));
//    }
//    
//    @Test
//    public void entity_with_subEntity_is_processed_correctly() {
//        List<NameableSubEntity> subEntities = new ArrayList<>();
//        subEntities.add(new NameableSubEntity("sub1"));
//        subEntities.add(new NameableSubEntity("sub2"));
//        root.setSubEntities(subEntities);
//        
//        TreeNode treeRepresentation = new TreeNode(root,"","leaf");
//        assertThat(treeRepresentation.getName(),is("root"));
//        assertThat(treeRepresentation.getSubNodes().size(),is(2));
//        assertThat(treeRepresentation.getSubNodes().get(0).getName(),is("sub1"));
//        assertThat(treeRepresentation.getSubNodes().get(1).getName(),is("sub2"));
//    }
//}
