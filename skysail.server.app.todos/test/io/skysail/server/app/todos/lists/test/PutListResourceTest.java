package io.skysail.server.app.todos.lists.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.todos.*;
import io.skysail.server.app.todos.repo.TodosRepository;

import org.junit.Test;

public class PutListResourceTest extends TodoListResourceTest {

//    private TodoList aList;
//
//    @Before
//    public void setUp() throws Exception {
//        super.setUpFixture();
//        super.setUpApplication(Mockito.mock(TodoApplication.class));
//        super.setUpResource(putTodoResource);
//        super.setUpResource(postTodoResource);
//        super.setUpRepository(new TodosRepository());
//        super.setUpSubject("admin");
//
//        new UniquePerOwnerValidator().setDbService(testDb);
//
//        aList = createList();
//        getAttributes().clear();
//    }

    @Test
    public void empty_form_data_yields_validation_failure() {
        TodoList aList = createList();

        form.add("name", "");
        form.add("id", aList.getId());
        getAttributes().put(TodoApplication.LIST_ID, aList.getId());
        putListResource.init(null, request, responses.get(putListResource.getClass().getName()));

        SkysailResponse<TodoList> skysailResponse = putListResource.put(form, HTML_VARIANT);

        assertValidationFailure(putListResource, skysailResponse,  "name", "size must be between");
    }

//    @Test
//    @Ignore
//    public void empty_json_data_yields_validation_failure() {
//        putTodoResource.getRequestAttributes().put(TodoApplication.LIST_ID, aList.getId());
//        getAttributes().put(TodoApplication.LIST_ID, aList.getId());
//        putTodoResource.init(null, request, response);
//
//        SkysailResponse<TodoList> skysailResponse = putTodoResource.put(new TodoList(), JSON_VARIANT);
//
//        assertValidationFailure(skysailResponse,  "name", "size must be between");
//    }

    @Test
    public void list_can_be_updated() {
        TodoList aList = createList();

        form.add("name", "updated_list");
        form.add("desc", "description");
        getAttributes().put(TodoApplication.LIST_ID, aList.getId());
        putListResource.init(null, request, responses.get(putListResource.getClass().getName()));

        putListResource.put(form, HTML_VARIANT);

        TodoList listFromDb = new TodosRepository().getById(TodoList.class, aList.getId());

        assertThat(listFromDb.getModified(), is(notNullValue()));
        assertThat(listFromDb.getCreated(), is(not(nullValue())));
        assertThat(listFromDb.getName(), is(equalTo("updated_list")));
        assertThat(listFromDb.getDesc(), is(equalTo("description")));
    }

}
