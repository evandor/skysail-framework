package io.skysail.server.app.todos.todos.resources.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.restlet.data.Status;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.todos.TodoApplication;
import io.skysail.server.app.todos.TodoList;
import io.skysail.server.app.todos.todos.Todo;

@Ignore

public class PostTodoResourceTest extends AbstractTodoResourceTest {

    private TodoList aList;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        aList = createList();
        setAttributes(TodoApplication.LIST_ID, aList.getId());
    }

    @Test
    public void empty_html_form_yields_validation_failure() {
        form.add("title", "");
        form.add("list", aList.getId());
        SkysailResponse<Todo> post = postTodoResource.post(form, HTML_VARIANT);
        assertSingleValidationFailure(postTodoResource, post, "title", "size must be between");
    }

    @Test
    public void start_date_after_due_date_yields_validation_failure() {
        form.add("title", "title_" + randomString());
        form.add("list", aList.getId());
        form.add("due", "2099-12-01");
        form.add("startDate", "2100-12-30");

        SkysailResponse<Todo> post = postTodoResource.post(form, HTML_VARIANT);
        assertSingleValidationFailure(postTodoResource, post, "", "the start date must be before the due date");
    }

    @Test
    public void valid_form_data_yields_new_entity() {
        String title = "title_" + randomString();
        form.add("title", title);
        form.add("list", aList.getId());
        form.add("due", "2099-12-01");

        SkysailResponse<Todo> result = postTodoResource.post(form, HTML_VARIANT);

        assertTodoResult(postTodoResource, result, title);
    }

    @Test
    public void valid_json_data_yields_new_entity() {
        String title = "title_" + randomString();
        Todo todo = new Todo(title);
        //todo.setParent(aList.getId());
        todo.setDue(Date.from(LocalDate.now().plusMonths(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        SkysailResponse<Todo> result = postTodoResource.post(todo, HTML_VARIANT);

        assertTodoResult(postTodoResource, result, title);
    }

    @Test
    public void urgency_is_calculated() {
        //TodoList aList = createList();
        form.add("title", "title_" + randomString());
        form.add("list", aList.getId());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        form.add("startDate", sdf.format(nowPlusWeeksAndDays(-1, -1)));
        form.add("due", sdf.format(nowPlusWeeks(1)));
        SkysailResponse<Todo> post = postTodoResource.post(form, HTML_VARIANT);
        assertThat(responses.get(postTodoResource.getClass().getName()).getStatus(), is(equalTo(Status.REDIRECTION_SEE_OTHER)));
        assertThat(post.getEntity().getUrgency(), greaterThan(0));
        assertThat(post.getEntity().getUrgency(), not(greaterThan(100)));
    }

    private Date nowPlusWeeks(int weeksOffset) {
        return Date.from(LocalDate.now().plusWeeks(weeksOffset).atStartOfDay().toInstant(ZoneOffset.MIN));
    }

    private Date nowPlusWeeksAndDays(int weeksOffset, int dayOffset) {
        return Date.from(LocalDate.now().plusWeeks(weeksOffset).plusDays(dayOffset).atStartOfDay()
                .toInstant(ZoneOffset.MIN));
    }

}
