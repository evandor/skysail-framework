package io.skysail.server.app.todos.lists.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.todos.*;
import io.skysail.server.app.todos.lists.*;
import io.skysail.server.app.todos.repo.TodosRepository;

import java.util.HashMap;

import org.apache.shiro.subject.SimplePrincipalMap;
import org.junit.*;
import org.mockito.*;
import org.restlet.data.*;
import org.restlet.engine.resource.VariantInfo;

public class PutListResourceTest extends ListResourceTest {

    @Spy
    private PutListResource resource;
    
    private TodosRepository repo;

    private TodoList aList;

    private VariantInfo htmlVariant = new VariantInfo(MediaType.TEXT_HTML);

    @Before
    public void setUp() throws Exception {
        super.setUp(Mockito.mock(TodoApplication.class), resource);

        repo = new TodosRepository();
        repo.setDbService(testDb);
        repo.activate();
        ((TodoApplication)application).setRepository(repo);
        Mockito.when(((TodoApplication)application).getRepository()).thenReturn(repo);
        
        Mockito.when(subjectUnderTest.getPrincipal()).thenReturn("admin");
        Mockito.when(subjectUnderTest.getPrincipals()).thenReturn(new SimplePrincipalMap(new HashMap<>()));
        setSubject(subjectUnderTest);
        
        new UniquePerOwnerValidator().setDbService(testDb);
        
        aList = createList();
    }

    @Test
    public void empty_form_data_yields_validation_failure() {
        form.add("name", "");
        form.add("id", aList.getId());
        resource.getRequestAttributes().put(TodoApplication.LIST_ID, aList.getId());
        resource.init(null, request, response);
        
        SkysailResponse<TodoList> skysailResponse = resource.put(form, HTML_VARIANT);

        assertValidationFailure(skysailResponse,  "name", "size must be between");
    }

    @Test
    public void empty_json_data_yields_validation_failure() {
        resource.getRequestAttributes().put(TodoApplication.LIST_ID, aList.getId());
        resource.init(null, request, response);
        
        SkysailResponse<TodoList> skysailResponse = resource.put(new TodoList(), JSON_VARIANT);

        assertValidationFailure(skysailResponse,  "name", "size must be between");
    }
    
    @Test
    public void list_can_be_updated() {
        form.add("name", "list3a");
        form.add("id", aList.getId());
        resource.getRequestAttributes().put(TodoApplication.LIST_ID, aList.getId());
        resource.init(null, request, response);
        
        resource.put(form, htmlVariant);
        
        TodoList listFromDb = new TodosRepository().getById(TodoList.class, aList.getId());
        
        assertThat(response.getStatus(),is(equalTo(Status.SUCCESS_OK)));
        assertThat(listFromDb.getModified(), is(notNullValue()));
        assertThat(listFromDb.getName(), is(equalTo("list3a")));
    }

    private TodoList createList() {
        aList = new TodoList();
        aList.setName("list_" + randomString());
        String id = TodosRepository.add(aList).toString();
        aList.setId(id);
        return aList;
    }

}
