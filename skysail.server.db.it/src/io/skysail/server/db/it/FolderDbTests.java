package io.skysail.server.db.it;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.server.db.OutEdges;
import io.skysail.server.db.it.folder.*;
import io.skysail.server.db.it.folder.resources.*;
import io.skysail.server.testsupport.categories.LargeTests;

import java.util.*;

import org.junit.*;
import org.junit.experimental.categories.Category;
import org.restlet.representation.Variant;

/**
 * Testing an entity with a recursive relation (a folder with subfolders with subfolders...).
 */
@Category(LargeTests.class)
public class FolderDbTests extends DbIntegrationTests {

    private PostFolderResource postFolderResource;
    private FolderResource folderResource;
    private FoldersResource foldersResource;
    private PutFolderResource putFolderResource;
    private String uniqueTitle;

    @BeforeClass
    public static void beforeClass() {
        skysailApplication = FolderApplication.getInstance();
    }

    @Before
    public void setUp() {
        super.setUp();
        postFolderResource = new PostFolderResource();
        folderResource = new FolderResource();
        foldersResource = new FoldersResource();
        putFolderResource = new PutFolderResource();

        setupEntityResource(postFolderResource);
        setupEntityResource(folderResource);
        setupEntityResource(foldersResource);
        setupEntityResource(putFolderResource);

        uniqueTitle = Long.toString(new Date().getTime());
    }

    @Test
    public void html_variant_posts_new_folder() {
        Folder folder = postAndRetrieveById(createFolderWithSubfolders(uniqueTitle), HTML_VARIANT);
        assertThat(folder.getName(), is(uniqueTitle));
        assertThat(folder.getSubfolders().size(), is(0));
    }

    @Test
    public void html_variant_posts_new_folder_with_subfolder() {
        Folder folder = postAndRetrieveById(createFolderWithSubfolders(uniqueTitle, "sub"), HTML_VARIANT);
        assertFolderWithOneSubfolder(folder, uniqueTitle, "sub");
    }

    @Test
    public void json_variant_posts_new_folder_with_subfolder() {
        Folder folder = postAndRetrieveById(createFolderWithSubfolders(uniqueTitle, "sub"), JSON_VARIANT);
        assertFolderWithOneSubfolder(folder, uniqueTitle, "sub");
    }

    private Folder postAndRetrieveById(Folder folderStructure, Variant htmlVariant) {
        Folder rootFolder = postFolderResource.post(folderStructure, HTML_VARIANT).getEntity();
        request.addAttribute("id", rootFolder.getId());
        return folderResource.getEntity();
    }

    private Folder createFolderWithSubfolders(String title, String... subfolderTitles) {
        Folder mainFolder = new Folder(title);
        Arrays.asList(subfolderTitles).forEach(subfolderTitle -> {
            Folder subFolder = new Folder(subfolderTitle);
            OutEdges<Folder> outEdges = new OutEdges<>();
            outEdges.add(subFolder);
            mainFolder.setSubfolders(outEdges);
        });
        return mainFolder;
    }

    private void assertFolderWithOneSubfolder(Folder folder, String folderTitle, String subfolderTitle) {
        assertThat(folder.getId(), is(notNullValue()));
        assertThat(folder.getName(), is(folderTitle));
        assertThat(folder.getSubfolders().size(), is(1));
        assertThat(folder.getSubfolders().get(0).getName(), is(subfolderTitle));
        assertThat(folder.getSubfolders().get(0).getId(), is(notNullValue()));
    }

}
