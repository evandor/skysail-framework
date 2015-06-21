package io.skysail.server.app.todos.todos.resources.test;

import io.skysail.api.responses.*;
import io.skysail.server.app.todos.*;
import io.skysail.server.app.todos.lists.UniquePerOwnerValidator;
import io.skysail.server.app.todos.repo.TodosRepository;
import io.skysail.server.app.todos.todos.*;
import io.skysail.server.app.todos.todos.resources.PostTodoResource;
import io.skysail.server.testsupport.PostResourceTest;

import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;

import org.apache.shiro.subject.SimplePrincipalMap;
import org.junit.*;
import org.mockito.*;
import org.restlet.data.MediaType;
import org.restlet.engine.resource.VariantInfo;

public class PostTodoResourceTest extends PostResourceTest {

    @Spy
    private PostTodoResource resource;

    @Before
    public void setUp() throws Exception {
        super.setUp(Mockito.mock(TodoApplication.class), resource);

        TodosRepository repo = new TodosRepository();
        repo.setDbService(testDb);
        repo.activate();
        ((TodoApplication) application).setRepository(repo);
        Mockito.when(((TodoApplication) application).getRepository()).thenReturn(repo);

        Mockito.when(subjectUnderTest.getPrincipal()).thenReturn("admin");
        Mockito.when(subjectUnderTest.getPrincipals()).thenReturn(new SimplePrincipalMap(new HashMap<>()));
        setSubject(subjectUnderTest);

        new UniquePerOwnerValidator().setDbService(testDb);
        new ValidListIdValidator().setDbService(testDb);

    }

    @Test
    @Ignore
    public void empty_html_form_yields_validation_failure() {
        form.add("title", "title_" + randomString());
        ConstraintViolationsResponse<?> post = (ConstraintViolationsResponse<?>) resource.post(form, new VariantInfo(
                MediaType.TEXT_HTML));
        assertValidationFailure(post, "list", "may not be null");
    }

    @Test
    public void wrong_list_yields_validation_failure() { 
        form.add("title", "title_" + randomString());
        form.add("list", "list_" + randomString());
        ConstraintViolationsResponse<?> post = (ConstraintViolationsResponse<?>) resource.post(form, new VariantInfo(MediaType.TEXT_HTML));
        assertValidationFailure(post, "list", "This list does not exist or has another owner");
    }
    
    @Test
    public void start_date_after_due_date_yields_validation_failure() { 
        String id = createNewList();
        
        form.add("title", "title_" + randomString());
        form.add("list", id);
        form.add("due", "2099-12-01");
        form.add("startDate", "2100-12-30");
        
        ConstraintViolationsResponse<?> post = (ConstraintViolationsResponse<?>) resource.post(form, new VariantInfo(MediaType.TEXT_HTML));
        assertValidationFailure(post, "", "the start date must be before the due date");
    }

    @Test
    public void valid_data_yields_new_entity() {
        String id = createNewList();
        form.add("title", "title_" + randomString());
        form.add("list", id);
        form.add("due", "2099-12-01");
        SkysailResponse<Todo> response = resource.post(form, new VariantInfo(MediaType.TEXT_HTML));
//        assertThat(response.getStatus(), is(equalTo(Status.SUCCESS_CREATED)));
//        assertThat(todo.getImportance(), is(50));
    }

    @Test
    public void urgency_is_calculated() {
        String id = createNewList();
        form.add("title", "title_" + randomString());
        form.add("list", id);
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        form.add("startDate", sdf.format(nowPlusWeeksAndDays(-1,-1)));
        form.add("due", sdf.format(nowPlusWeeks(1)));
        SkysailResponse<Todo> response = resource.post(form, new VariantInfo(MediaType.TEXT_HTML));
//        assertThat(response.getStatus(), is(equalTo(Status.SUCCESS_CREATED)));
//        assertThat(todo.getUrgency(), greaterThan(0));
//        assertThat(todo.getUrgency(), not(greaterThan(100)));
    }

    private String createNewList() {
        TodoList entity = new TodoList();
        entity.setName("list_" + randomString());
        entity.setOwner("admin");
        String id = TodosRepository.add(entity).toString();
        return id;
    }
    
    private Date nowPlusWeeks(int weeksOffset) {
        return Date.from(LocalDate.now().plusWeeks(weeksOffset).atStartOfDay().toInstant(ZoneOffset.MIN));
    }
    
    private Date nowPlusWeeksAndDays(int weeksOffset, int dayOffset) {
        return Date.from(LocalDate.now().plusWeeks(weeksOffset).plusDays(dayOffset).atStartOfDay().toInstant(ZoneOffset.MIN));
    }

}
