package io.skysail.server.db.it;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.server.db.it.one2many.comment.Comment;
import io.skysail.server.db.it.one2many.todo.*;
import io.skysail.server.db.it.one2many.todo.resources.*;
import io.skysail.server.testsupport.categories.LargeTests;

import java.util.*;
import java.util.stream.Collectors;

import org.junit.*;
import org.junit.experimental.categories.Category;

/**
 * TODO: what about the JSON-variants?
 *
 */

@Category(LargeTests.class)
public class OneToManyDbTests extends DbIntegrationTests {

    private PostTodoResource postTodoResource;
    private TodoResource todoResource;
    private TodosResource todosResource;
    private PutTodoResource putTodoResource;

    @BeforeClass
    public static void beforeClass() {
        skysailApplication = TodoApplication.getInstance();
    }

    @Before
    public void setUp() {
        super.setUp();
        postTodoResource = new PostTodoResource();
        todoResource = new TodoResource();
        todosResource = new TodosResource();
        putTodoResource = new PutTodoResource();

        setupEntityResource(postTodoResource);
        setupEntityResource(todoResource);
        setupEntityResource(todosResource);
        setupEntityResource(putTodoResource);
    }

    @Test
    public void html_variant_posts_todo_without_comment() {
        String title = Long.toString(new Date().getTime());
        Todo todo = postTodoResource.post(createTodoWithComments(title), HTML_VARIANT).getEntity();
        assertThat(todo.getTitle(), is(title));
        assertThat(todo.getComments().size(), is(0));
    }

    @Test
    public void html_variant_posts_todo_with_comment() {
        String title = Long.toString(new Date().getTime());
        Todo todo = postTodoResource.post(createTodoWithComments(title, new Comment()), HTML_VARIANT).getEntity();
        assertNewTodoWithTitleAndComment(todo, title);
    }

    @Test
    public void html_variant_posts_todo_with_two_comments() {
        String title = Long.toString(new Date().getTime());
        Todo todo = postTodoResource.post(createTodoWithComments(title, new Comment(), new Comment()), HTML_VARIANT)
                .getEntity();
        List<Comment> comments = todo.getComments();
        assertThat(comments.size(), is(2));
        assertThat(comments.get(0).getComment(), is(not(comments.get(1).getComment())));
    }

    @Test
    public void html_variant_gets_a_Todo_without_comment() {
        String title = Long.toString(new Date().getTime());
        request.addAttribute("id", postTodoResource.post(createTodoWithComments(title), HTML_VARIANT).getEntity()
                .getId());
        Todo todoFromDb = todoResource.getEntity();
        assertThat(todoFromDb.getTitle(), is(title));
        assertThat(todoFromDb.getComments().size(), is(0));
    }

    @Test
    public void html_variant_gets_a_Todo_with_a_comment() {
        String title = Long.toString(new Date().getTime());
        request.addAttribute("id", postTodoResource.post(createTodoWithComments(title, new Comment()), HTML_VARIANT)
                .getEntity().getId());
        Todo todoFromDb = todoResource.getEntity();
        assertThat(todoFromDb.getTitle(), is(title));
        assertThat(todoFromDb.getComments().get(0).getComment(), is(notNullValue()));
    }

    @Test
    public void html_variant_gets_a_Todo_with_two_comments() {
        String title = Long.toString(new Date().getTime());
        request.addAttribute("id",
                postTodoResource.post(createTodoWithComments(title, new Comment(), new Comment()), HTML_VARIANT)
                        .getEntity().getId());
        Todo todoFromDb = todoResource.getEntity();
        assertThat(todoFromDb.getTitle(), is(title));
        assertThat(todoFromDb.getComments().size(), is(2));
    }

    @Test
    public void html_variant_gets_the_list_of_Todos() {
        String title = Long.toString(new Date().getTime());
        postTodoResource.post(createTodoWithComments(title, new Comment()), HTML_VARIANT);
        postTodoResource.post(createTodoWithComments(title, new Comment()), HTML_VARIANT);

        List<Todo> todos = todosResource.getEntities(HTML_VARIANT).getEntity();

        List<Todo> byTestCreatedTodos = todos.stream().filter(Todo -> Todo.getTitle().equals(title))
                .collect(Collectors.toList());
        assertThat(byTestCreatedTodos.size(), is(2));
        assertThat(byTestCreatedTodos.get(0).getComments().get(0).getComment(), is(notNullValue()));
    }

    @Test
    public void html_variant_updates_title_and_comment() {
        String title = Long.toString(new Date().getTime());
        String todoId = postTodoResource.post(createTodoWithComments(title, new Comment()), HTML_VARIANT).getEntity()
                .getId();
        request.addAttribute("id", todoId);
        Todo todoFromDb = todoResource.getEntity();

        todoFromDb.setTitle("changed");
        todoFromDb.getComments().get(0).setComment("a changed comment, too");

        putTodoResource.putEntity(todoFromDb, HTML_VARIANT).getEntity();

        Todo todoFromDb2 = todoResource.getEntity();
        assertThat(todoFromDb2.getModified(), is(notNullValue()));
        assertThat(todoFromDb2.getTitle(), is("changed"));
        assertThat(todoFromDb2.getComments().get(0).getComment(), is("a changed comment, too"));
    }

    @Test
    public void html_variant_adds_comment() {
        String title = Long.toString(new Date().getTime());
        String todoId = postTodoResource.post(createTodoWithComments(title, new Comment()), HTML_VARIANT).getEntity()
                .getId();
        request.addAttribute("id", todoId);
        Todo todoFromDb = todoResource.getEntity();

        todoFromDb.getComments().add(new Comment());

        putTodoResource.putEntity(todoFromDb, HTML_VARIANT).getEntity();

        assertThat(todoResource.getEntity().getComments().size(), is(2));
    }

    @Test
    public void html_variant_deletes_comment() {
        String title = Long.toString(new Date().getTime());
        String todoId = postTodoResource.post(createTodoWithComments(title, new Comment()), HTML_VARIANT).getEntity()
                .getId();
        request.addAttribute("id", todoId);
        Todo todoFromDb = todoResource.getEntity();

        todoFromDb.setComments(new ArrayList<>());

        putTodoResource.putEntity(todoFromDb, HTML_VARIANT).getEntity();

        assertThat(todoResource.getEntity().getComments().size(), is(0));
    }

    @Test
    public void html_variant_deletes_todo_including_comment() {
        String title = Long.toString(new Date().getTime());
        String id = postTodoResource.post(createTodoWithComments(title, new Comment()), HTML_VARIANT).getEntity()
                .getId();
        request.addAttribute("id", id);
        String commentId = todoResource.getEntity().getComments().get(0).getId();

        todoResource.deleteEntity(HTML_VARIANT);

        // -- seems still to be available here...???
        Todo todoFromDb = todoResource.getEntity();
        assertThat(todoFromDb, is(nullValue()));

        assertThat(dbService.findById2(Comment.class, commentId), is(nullValue()));
    }

    private Todo createTodoWithComments(String name, Comment... comments) {
        Todo todo = new Todo(name);
        Arrays.stream(comments).forEach(comment -> todo.getComments().add(comment));
        return todo;
    }

    private void assertNewTodoWithTitleAndComment(Todo todo, String title) {
        assertThat(todo.getTitle(), is(title));
        assertThat(todo.getCreated(), is(notNullValue()));
        assertThat(todo.getModified(), is(nullValue()));
        assertThat(todo.getComments().size(), is(1));
        assertThat(todo.getComments().get(0).getComment(), is(notNullValue()));
    }

}
