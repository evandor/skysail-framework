package io.skysail.server.model.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.*;
import org.restlet.data.Reference;

import io.skysail.domain.Nameable;
import io.skysail.server.model.TreeStructure;
import io.skysail.server.restlet.resources.SkysailServerResource;
import lombok.Data;

public class TreeStructureTest {

    @Data
    private class AFolder implements Nameable {
        private String name, id;

        public AFolder(String name) {
            this.name = name;
        }
    }

    private SkysailServerResource<?> resource;
    private AFolder rootFolder;
    private List<TreeStructure> resourceTreeRepresentation;
    private Reference theReference;

    @Before
    public void setUp() {
        rootFolder = new AFolder("root");
        resource = mock(SkysailServerResource.class);
        theReference = mock(Reference.class);
        resourceTreeRepresentation = new ArrayList<>();
        when(resource.getTreeRepresentation()).thenReturn(resourceTreeRepresentation);
        when(resource.getReference()).thenReturn(theReference);
    }

    @Test
    public void resource_without_treeStructureInfo_yields_empty_tree() {
        List<TreeStructure> treeStructureList = TreeStructure.from(resource);
        assertThat(treeStructureList.size(), is(0));
    }

    @Test
    public void resource_with_empty_treeStructureInfo_yields_empty_tree() {
        List<TreeStructure> treeStructureList = TreeStructure.from(resource);
        assertThat(treeStructureList.size(), is(0));
    }

    @Test
    public void resource_with_simple_treeStructureInfo_yields_one_element_tree() {
        resourceTreeRepresentation.add(new TreeStructure(rootFolder, "link", "glyph"));
        when(theReference.getSegments()).thenReturn(Arrays.asList("seg1", "seg2", "seg3"));

        List<TreeStructure> treeStructureList = TreeStructure.from(resource);
        
        assertThat(treeStructureList.size(), is(1));
        assertTreeStructure(treeStructureList.get(0), "root", "/seg1/seg2/seg3#", "glyph", 0);
    }

//    @Test
//    public void resource_with_empty_treeStructureInfo_yields_empty_tree2() {
//        TreeStructure rootTreeRepresentation = new TreeStructure(rootFolder, "link", "glyph");
//        rootTreeRepresentation.add(new TreeStructure(new AFolder("sub"), "sublink", "glyph2"));
//        resourceTreeRepresentation.add(rootTreeRepresentation);
//        when(theReference.getSegments()).thenReturn(Arrays.asList("seg1", "seg2", "seg3"));
//        
//        List<TreeStructure> treeStructureList = TreeStructure.from(resource);
//
//        assertThat(treeStructureList.size(), is(1));
//        assertTreeStructure(treeStructureList.get(0), "root", "/seg1/seg2/seg3#", "glyph", 1);
//        assertTreeStructure(treeStructureList.get(0).getSubfolders().get(0), "sub", "/seg1/seg2/seg3#", "glyph2", 0);
//    }

    private void assertTreeStructure(TreeStructure treeStructure, String name, String link, String glyph, int size) {
        assertThat(treeStructure.getName(), is(name));
        assertThat(treeStructure.getLink(), is(link));
        assertThat(treeStructure.getGlyph(), is(glyph));
        assertThat(treeStructure.getSubfolders().size(), is(size));
    }

}
