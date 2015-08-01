package io.skysail.server.app.todos.todos.resources.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.server.app.todos.*;
import io.skysail.server.app.todos.todos.Todo;

import org.junit.*;
import org.mockito.Mockito;
import org.restlet.data.*;
import org.restlet.engine.resource.VariantInfo;

@Ignore
public class PutTodoResourceTest extends AbstractTodoResourceTest {

    private TodoList aList;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        aList = createList();
        getAttributes().put(TodoApplication.LIST_ID, aList.getId());
    }

//    @Before
//    public void setUp() throws Exception {
//        super.setUpFixture();
//        super.setUpApplication(Mockito.mock(TodoApplication.class));
//        super.setUpResource(resource);
//
//        repo = new TodosRepository();
//        repo.setDbService(testDb);
//        repo.activate();
//        ((TodoApplication) application).setRepository(repo);
//        Mockito.when(((TodoApplication) application).getRepository()).thenReturn(repo);
//
//        Mockito.when(subjectUnderTest.getPrincipal()).thenReturn("admin");
//        Mockito.when(subjectUnderTest.getPrincipals()).thenReturn(new SimplePrincipalMap(new HashMap<>()));
//        setSubject(subjectUnderTest);
//
//        new UniquePerOwnerValidator().setDbService(testDb);
//        new ValidListIdValidator().setDbService(testDb);
//
//        // super.setUp(new TodoApplication(), resource);
//        //
//        // Mockito.doReturn(application).when(resource).getApplication();
//        // AtomicReference<ValidatorService> validatorServiceRef = new
//        // AtomicReference<>();
//        // ValidatorService validatorService =
//        // Mockito.mock(ValidatorService.class);
//        // Validator validator = Mockito.mock(Validator.class);
//        // validatorServiceRef.set(validatorService);
//        // Mockito.doReturn(validator).when(validatorService).getValidator();
//        // Mockito.doReturn(validatorServiceRef).when(application).getValidatorService();
//        // Mockito.doReturn(query).when(resource).getQuery();
//        //
//        // resource.init(null, request, response);
//    }

    @Test
    @Ignore
    public void rejects_updating_password_if_old_password_is_null() throws Exception {
        Mockito.when(subjectUnderTest.getPrincipal()).thenReturn("admin");
        setSubject(subjectUnderTest);
        // resource.put(form, null);
        // assertThat(response.getStatus().getCode(), is(400));
    }

    @Test
    public void todo_can_be_updated() {
        Todo todo = createTodo(aList);

        form.add("title", "changed");
        form.add("status", io.skysail.server.app.todos.todos.status.Status.WIP.name());
        form.add("id", todo.getId());
        form.add("parent", aList.getId());
        putTodoResource.getRequestAttributes().put(TodoApplication.LIST_ID, aList.getId());
        putTodoResource.getRequestAttributes().put(TodoApplication.TODO_ID, todo.getId());
        putTodoResource.init(null, request, responses.get(putTodoResource.getClass().getName()));

        putTodoResource.put(form, new VariantInfo(MediaType.TEXT_HTML));

        Todo entityFromDb = repo.getById(Todo.class, todo.getId());

        assertThat(responses.get(putTodoResource.getClass().getName()).getStatus(), is(equalTo(Status.SUCCESS_OK)));
        assertThat(entityFromDb.getModified(), is(notNullValue()));
        assertThat(entityFromDb.getTitle(), is(equalTo("changed")));
        assertThat(entityFromDb.getStatus(), is(equalTo(io.skysail.server.app.todos.todos.status.Status.WIP)));
    }

}
