package io.skysail.server.app.todos.lists.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.api.responses.*;
import io.skysail.server.app.todos.TodoList;

import org.junit.Test;
import org.restlet.data.*;
import org.restlet.engine.resource.VariantInfo;

public class PostListResourceTest extends AbstractListResourceTest {

    @Test
    public void valid_json_data_yields_new_entity() {
        SkysailResponse<TodoList> result = postListresource.post(new TodoList("jsonList1"), JSON_VARIANT);
        assertThat(responses.get(postListresource.getClass().getName()).getStatus(),is(equalTo(Status.SUCCESS_CREATED)));
        assertListResult(postListresource, result, "jsonList1");
        assertThat(result.getEntity().getCreated(), is(notNullValue()));
        assertThat(result.getEntity().getOwner(), is("admin"));
    }

    @Test
    public void two_entries_with_same_name_yields_failure() {
        form.add("name", "two_entries_with_same_name_yields_failure");
        postListresource.post(form, new VariantInfo(MediaType.TEXT_HTML));
        ConstraintViolationsResponse<?> post = (ConstraintViolationsResponse<?>) postListresource.post(form, HTML_VARIANT);
        assertSingleValidationFailure(postListresource, post,  "", "name already exists");
    }

    @Test
    public void new_list_with_default_flag_becomes_default() {
        form.add("name", "new_list_with_default_flag_becomes_default");
        form.add("defaultList", "on");
        SkysailResponse<TodoList> result = postListresource.post(form, HTML_VARIANT);

        assertThat(responses.get(postListresource.getClass().getName()).getStatus(),is(Status.REDIRECTION_SEE_OTHER));
        assertThat(result.getEntity().isDefaultList(),is(true));
    }

    @Test
    public void users_first_list_becomes_default_even_if_not_flagged_as_such() {
        setUpSubject("user_users_first_list_becomes_default_even_if_not_flagged_as_such");
        form.add("name", "name_users_first_list_becomes_default_even_if_not_flagged_as_such");
        SkysailResponse<TodoList> result = postListresource.post(form, HTML_VARIANT);

        assertThat(responses.get(postListresource.getClass().getName()).getStatus(),is(Status.REDIRECTION_SEE_OTHER));
        assertThat(result.getEntity().isDefaultList(),is(true));
    }

    @Test
    public void second_list_with_default_flag_toggles_first_one() {
        setUpSubject("user_second_list_with_default_flag_toggles_first_one");

        form.add("name", "second_list_with_default_flag_toggles_first_one1");
        form.add("defaultList", "on");
        String id1 = postListresource.post(form, HTML_VARIANT).getEntity().getId();

        form.clear();
        form.add("name", "second_list_with_default_flag_toggles_first_one2");
        form.add("defaultList", "on");
        String id2 = postListresource.post(form, HTML_VARIANT).getEntity().getId();

        TodoList id1TodoList = (TodoList) listRepo.getVertexById(id1);
        TodoList is2TodoList = (TodoList) listRepo.getVertexById(id2);

        assertThat(id1TodoList.isDefaultList(), is(false));
        assertThat(is2TodoList.isDefaultList(), is(true));
    }

}

