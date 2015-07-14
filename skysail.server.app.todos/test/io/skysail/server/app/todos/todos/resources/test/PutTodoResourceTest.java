package io.skysail.server.app.todos.todos.resources.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.server.app.todos.*;
import io.skysail.server.app.todos.lists.UniquePerOwnerValidator;
import io.skysail.server.app.todos.repo.TodosRepository;
import io.skysail.server.app.todos.todos.*;
import io.skysail.server.app.todos.todos.resources.PutTodoResource;
import io.skysail.server.testsupport.ResourceTestBase;

import java.util.HashMap;

import org.apache.shiro.subject.SimplePrincipalMap;
import org.junit.*;
import org.mockito.*;
import org.restlet.data.*;
import org.restlet.engine.resource.VariantInfo;

public class PutTodoResourceTest extends ResourceTestBase {

    @Spy
    private PutTodoResource resource;


    private TodosRepository repo;

    @Before
    public void setUp() throws Exception {
        super.setUpFixture();
        super.setUpApplication(Mockito.mock(TodoApplication.class));
        super.setUpResource(resource);

        repo = new TodosRepository();
        repo.setDbService(testDb);
        repo.activate();
        ((TodoApplication) application).setRepository(repo);
        Mockito.when(((TodoApplication) application).getRepository()).thenReturn(repo);

        Mockito.when(subjectUnderTest.getPrincipal()).thenReturn("admin");
        Mockito.when(subjectUnderTest.getPrincipals()).thenReturn(new SimplePrincipalMap(new HashMap<>()));
        setSubject(subjectUnderTest);

        new UniquePerOwnerValidator().setDbService(testDb);
        new ValidListIdValidator().setDbService(testDb);

        // super.setUp(new TodoApplication(), resource);
        //
        // Mockito.doReturn(application).when(resource).getApplication();
        // AtomicReference<ValidatorService> validatorServiceRef = new
        // AtomicReference<>();
        // ValidatorService validatorService =
        // Mockito.mock(ValidatorService.class);
        // Validator validator = Mockito.mock(Validator.class);
        // validatorServiceRef.set(validatorService);
        // Mockito.doReturn(validator).when(validatorService).getValidator();
        // Mockito.doReturn(validatorServiceRef).when(application).getValidatorService();
        // Mockito.doReturn(query).when(resource).getQuery();
        //
        // resource.init(null, request, response);
    }

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

        String listId = createNewList();
        String todoTitle = "title_" + randomString();

        Todo todo = new Todo();
        todo.setList(listId);
        todo.setOwner("admin");
        todo.setStatus(io.skysail.server.app.todos.todos.status.Status.NEW);
        todo.setTitle(todoTitle);

        String id = repo.add(todo).toString();

        form.add("title", todoTitle + "_New");
        form.add("status", io.skysail.server.app.todos.todos.status.Status.WIP.name());
        form.add("id", id);
        resource.getRequestAttributes().put(TodoApplication.LIST_ID, listId);
        resource.getRequestAttributes().put(TodoApplication.TODO_ID, id);
        resource.init(null, request, responses.get(resource.getClass().getName()));

        resource.put(form, new VariantInfo(MediaType.TEXT_HTML));

        Todo entityFromDb = repo.getById(Todo.class, id);

        assertThat(responses.get(resource.getClass().getName()).getStatus(), is(equalTo(Status.SUCCESS_OK)));
        assertThat(entityFromDb.getModified(), is(notNullValue()));
        assertThat(entityFromDb.getTitle(), is(equalTo(todoTitle + "_New")));
        assertThat(entityFromDb.getStatus(), is(equalTo(io.skysail.server.app.todos.todos.status.Status.WIP)));
    }

    private String createNewList() {
        TodoList list = new TodoList();
        list.setName("list_" + randomString());
        list.setOwner("admin");
        String id = TodosRepository.add(list).toString();
        return id;
    }

}
