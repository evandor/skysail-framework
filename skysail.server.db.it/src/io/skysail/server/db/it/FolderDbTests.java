package io.skysail.server.db.it;

import io.skysail.server.db.it.folder.FolderApplication;
import io.skysail.server.db.it.folder.resources.*;
import io.skysail.server.testsupport.categories.LargeTests;

import org.junit.*;
import org.junit.experimental.categories.Category;

@Category(LargeTests.class)
public class FolderDbTests extends DbIntegrationTests {

    private PostFolderResource postFolderResource;
    private FolderResource todoResource;
    private FoldersResource todosResource;
    private PutFolderResource putFolderResource;

    @BeforeClass
    public static void beforeClass() {
        skysailApplication = FolderApplication.getInstance();
    }

    @Before
    public void setUp() throws Exception {
        postFolderResource = new PostFolderResource();
        todoResource = new FolderResource();
        todosResource = new FoldersResource();
        putFolderResource = new PutFolderResource();

        setupEntityResource(postFolderResource);
        setupEntityResource(todoResource);
        setupEntityResource(todosResource);
        setupEntityResource(putFolderResource);

        request.clearAttributes();
    }

//    @Test
//    public void html_variant_posts_todo_without_comment() {
//        String title = Long.toString(new Date().getTime());
//        Folder todo = postFolderResource.post(createFolderWithComments(title), HTML_VARIANT).getEntity();
//        assertThat(todo.getName(), is(title));
////        assertThat(todo.getComments().size(), is(0));
//    }


//    private Folder createFolderWithComments(String name, Comment... comments) {
//        Folder todo = new Folder(name);
//        Arrays.stream(comments).forEach(comment -> todo.getComments().add(comment));
//        return todo;
//    }
//
//    private void assertNewFolderWithTitleAndComment(Folder todo, String title) {
//        assertThat(todo.getTitle(), is(title));
//        assertThat(todo.getCreated(), is(notNullValue()));
//        assertThat(todo.getModified(), is(nullValue()));
//        assertThat(todo.getComments().size(), is(1));
//        assertThat(todo.getComments().get(0).getComment(), is(notNullValue()));
//    }

}
