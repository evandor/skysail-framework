package io.skysail.server.db.it;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.server.db.it.one2many.comment.Comment;
import io.skysail.server.db.it.one2many.todo.*;
import io.skysail.server.db.it.one2many.todo.resources.*;

import java.util.*;
import java.util.stream.Collectors;

import org.junit.*;

/**
 * TODO: what about the JSON-variants?
 *
 */
public class OneToManyDbTests extends DbIntegrationTests {

    private PostTodoResource postTodoResource;
    private TodoResource todoResource;
    private TodosResource todosResource;
    private PutTodoResource putTodoResource;

    private Todo aNewTodo;

    @BeforeClass
    public static void beforeClass() {
        skysailApplication = TodoApplication.getInstance();
    }

    @Before
    public void setUp() throws Exception {
        postTodoResource = new PostTodoResource();
        todoResource = new TodoResource();
        todosResource = new TodosResource();
        putTodoResource = new PutTodoResource();

        setupEntityResource(postTodoResource);
        setupEntityResource(todoResource);
        setupEntityResource(todosResource);
        setupEntityResource(putTodoResource);

        request.clearAttributes();
        aNewTodo = createTodoWithComments(new Comment());
    }

    @Test
    public void html_variant_creates_a_new_todo_without_comment() {
        Todo todo = postTodoResource.post(createTodoWithComments(), HTML_VARIANT).getEntity();
        assertThat(todo.getTitle(),is(aNewTodo.getTitle()));
        assertThat(todo.getComments().size(),is(0));
    }

    @Test
    public void html_variant_creates_a_new_todo_with_comment() {
        Todo todo = postTodoResource.post(aNewTodo, HTML_VARIANT).getEntity();
        assertNewTodoWithTitleAndComment(todo, todo.getTitle());
    }

    @Test
    public void html_variant_creates_a_new_with_two_comments() {
        Todo todo = postTodoResource.post(createTodoWithComments(new Comment(), new Comment()), HTML_VARIANT).getEntity();
        List<Comment> comments = todo.getComments();
        assertThat(comments.size(),is(2));
        assertThat(comments.get(0).getComment(),is(not(comments.get(1).getComment())));
    }

    @Test
    public void html_variant_retrieves_a_Todo_without_comment_by_id() {
        request.addAttribute("id", postTodoResource.post(createTodoWithComments(), HTML_VARIANT).getEntity().getId());
        Todo todoFromDb = todoResource.getEntity();
        assertThat(todoFromDb.getTitle(),is(aNewTodo.getTitle()));
        assertThat(todoFromDb.getComments().size(),is(0));
    }

    @Test
    public void html_variant_retrieves_a_Todo_by_id() {
        request.addAttribute("id", postTodoResource.post(aNewTodo, HTML_VARIANT).getEntity().getId());
        Todo todoFromDb = todoResource.getEntity();
        assertThat(todoFromDb.getTitle(),is(aNewTodo.getTitle()));
        assertThat(todoFromDb.getComments().get(0).getComment(),is(notNullValue()));
    }

    @Test
    public void html_variant_retrieves_a_Todo_with_two_comments_by_id() {
        request.addAttribute("id", postTodoResource.post(createTodoWithComments(new Comment(),new Comment()), HTML_VARIANT).getEntity().getId());
        Todo todoFromDb = todoResource.getEntity();
        assertThat(todoFromDb.getTitle(),is(aNewTodo.getTitle()));
        assertThat(todoFromDb.getComments().size(),is(2));
    }

    @Test
    public void html_variant_retrieves_the_list_of_Todos() {
        postTodoResource.post(aNewTodo, HTML_VARIANT);
        postTodoResource.post(aNewTodo, HTML_VARIANT);
        List<Todo> todos = todosResource.getEntities(HTML_VARIANT).getEntity();
        List<Todo> byTestCreatedTodos = todos.stream().filter(Todo -> Todo.getTitle().equals(aNewTodo.getTitle())).collect(Collectors.toList());
        assertThat(byTestCreatedTodos.size(),is(2));
        assertThat(byTestCreatedTodos.get(0).getComments().get(0).getComment(),is(notNullValue()));
    }

    @Test
    public void html_variant_todo_can_be_updated() {
        String todoId = postTodoResource.post(aNewTodo, HTML_VARIANT).getEntity().getId();
        request.addAttribute("id", todoId);
        Todo todoFromDb = todoResource.getEntity();

        todoFromDb.setTitle("changed");
        todoFromDb.getComments().get(0).setComment("a changed comment, too");

        putTodoResource.putEntity(todoFromDb, HTML_VARIANT).getEntity();

        Todo todoFromDb2 = todoResource.getEntity();
        assertThat(todoFromDb2.getModified(),is(notNullValue()));
        assertThat(todoFromDb2.getTitle(),is("changed"));
        assertThat(todoFromDb2.getComments().get(0).getComment(),is("a changed comment, too"));
    }

    @Test
    public void html_variant_todo_can_be_deleted_again() {
        String id = postTodoResource.post(aNewTodo, HTML_VARIANT).getEntity().getId();
        request.addAttribute("id", id);
        String commentId = todoResource.getEntity().getComments().get(0).getId();

        todoResource.deleteEntity(HTML_VARIANT);

        // -- seems still to be available here...???
        Todo todoFromDb = todoResource.getEntity();
        assertThat(todoFromDb,is(nullValue()));

        assertThat(dbService.findById(Comment.class, commentId),is(nullValue()));
    }

    private Todo createTodoWithComments(Comment... comments) {
        String name = Long.toString(new Date().getTime());
        Todo todo = new Todo(name);
        Arrays.stream(comments).forEach(comment -> todo.getComments().add(comment));
        return todo;
    }

    private void assertNewTodoWithTitleAndComment(Todo todo, String title) {
        assertThat(todo.getTitle(),is(aNewTodo.getTitle()));
        assertThat(todo.getCreated(),is(notNullValue()));
        assertThat(todo.getModified(),is(nullValue()));
        assertThat(todo.getComments().size(),is(1));
        assertThat(todo.getComments().get(0).getComment(),is(notNullValue()));
    }


}
