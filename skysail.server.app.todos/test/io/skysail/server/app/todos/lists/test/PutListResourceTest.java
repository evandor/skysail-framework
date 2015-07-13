package io.skysail.server.app.todos.lists.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.todos.TodoApplication;
import io.skysail.server.app.todos.TodoList;
import io.skysail.server.app.todos.lists.UniquePerOwnerValidator;
import io.skysail.server.app.todos.repo.TodosRepository;

import java.util.HashMap;

import org.apache.shiro.subject.SimplePrincipalMap;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

public class PutListResourceTest extends ListResourceTest {

    private TodosRepository repo;

    private TodoList aList;

    @Before
    public void setUp() throws Exception {
        super.setUp(Mockito.mock(TodoApplication.class));
        super.setUp(putListResource);

        repo = new TodosRepository();
        repo.setDbService(testDb);
        repo.activate();
        ((TodoApplication)application).setRepository(repo);
        Mockito.when(((TodoApplication)application).getRepository()).thenReturn(repo);

        Mockito.when(subjectUnderTest.getPrincipal()).thenReturn("admin");
        Mockito.when(subjectUnderTest.getPrincipals()).thenReturn(new SimplePrincipalMap(new HashMap<>()));
        setSubject(subjectUnderTest);

        new UniquePerOwnerValidator().setDbService(testDb);

        super.setUp(postListresource);
        aList = createList();
        getAttributes().clear();
    }

    @Test
    public void empty_form_data_yields_validation_failure() {
        form.add("name", "");
        form.add("id", aList.getId());
        //putListResource.getRequestAttributes().put(TodoApplication.LIST_ID, aList.getId());
        getAttributes().put(TodoApplication.LIST_ID, aList.getId());
        putListResource.init(null, request, response);

        SkysailResponse<TodoList> skysailResponse = putListResource.put(form, HTML_VARIANT);

        assertValidationFailure(skysailResponse,  "name", "size must be between");
    }

    @Test
    @Ignore
    public void empty_json_data_yields_validation_failure() {
        putListResource.getRequestAttributes().put(TodoApplication.LIST_ID, aList.getId());
        getAttributes().put(TodoApplication.LIST_ID, aList.getId());
        putListResource.init(null, request, response);

        SkysailResponse<TodoList> skysailResponse = putListResource.put(new TodoList(), JSON_VARIANT);

        assertValidationFailure(skysailResponse,  "name", "size must be between");
    }

    @Test
    public void list_can_be_updated() {
        form.add("name", "updated_list");
        form.add("desc", "description");
        getAttributes().put(TodoApplication.LIST_ID, aList.getId());
        putListResource.init(null, request, response);

        SkysailResponse<TodoList> put = putListResource.put(form, HTML_VARIANT);
        System.out.println(put);

        TodoList listFromDb = new TodosRepository().getById(TodoList.class, aList.getId());

        //assertThat(response.getStatus(),is(equalTo(Status.SUCCESS_OK)));
        assertThat(listFromDb.getModified(), is(notNullValue()));
        assertThat(listFromDb.getCreated(), is(not(nullValue())));
        assertThat(listFromDb.getName(), is(equalTo("updated_list")));
        assertThat(listFromDb.getDesc(), is(equalTo("description")));
    }

    private TodoList createList() {
        TodoList aList = new TodoList();
        aList.setName("list_" + randomString());
        SkysailResponse<TodoList> post = postListresource.post(aList,JSON_VARIANT);
        return post.getEntity();
    }

}
