package io.skysail.server.app.todos.lists.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.restlet.data.*;
import org.restlet.engine.resource.VariantInfo;

import io.skysail.api.responses.*;
import io.skysail.server.app.todos.TodoList;

public class PostListResourceTest extends AbstractListResourceTest {

    @Test
    public void valid_json_data_yields_new_entity() {
        SkysailResponse<TodoList> result = postListResource.post(new TodoList("valid_json_data_yields_new_entity"), JSON_VARIANT);
        assertThat(responses.get(postListResource.getClass().getName()).getStatus(),is(equalTo(Status.SUCCESS_CREATED)));
        assertListResult(postListResource, result, "valid_json_data_yields_new_entity");
        assertThat(result.getEntity().getCreated(), is(notNullValue()));
        assertThat(result.getEntity().getOwner(), is("admin"));
    }

    @Test
    public void two_entries_with_same_name_yields_failure() {
        form.add("name", "two_entries_with_same_name_yields_failure");
        postListResource.post(form, new VariantInfo(MediaType.TEXT_HTML));
        ConstraintViolationsResponse<?> post = (ConstraintViolationsResponse<?>) postListResource.post(form, HTML_VARIANT);
        assertSingleValidationFailure(postListResource, post,  "", "name already exists");
    }

    @Test
    public void new_list_with_default_flag_becomes_default() {
        form.add("name", "new_list_with_default_flag_becomes_default");
        form.add("defaultList", "on");
        SkysailResponse<TodoList> result = postListResource.post(form, HTML_VARIANT);

        assertThat(responses.get(postListResource.getClass().getName()).getStatus(),is(Status.REDIRECTION_SEE_OTHER));
        assertThat(result.getEntity().isDefaultList(),is(true));
    }

    @Test
    public void users_first_list_becomes_default_even_if_not_flagged_as_such() {
        setUpSubject("user_users_first_list_becomes_default_even_if_not_flagged_as_such");
        form.add("name", "name_users_first_list_becomes_default_even_if_not_flagged_as_such");
        SkysailResponse<TodoList> result = postListResource.post(form, HTML_VARIANT);

        assertThat(responses.get(postListResource.getClass().getName()).getStatus(),is(Status.REDIRECTION_SEE_OTHER));
        assertThat(result.getEntity().isDefaultList(),is(true));
    }

    @Test
    public void second_list_with_default_flag_toggles_first_one() {
        setUpSubject("user_second_list_with_default_flag_toggles_first_one");

        form.add("name", "second_list_with_default_flag_toggles_first_one1");
        form.add("defaultList", "on");
        String id1 = postListResource.post(form, HTML_VARIANT).getEntity().getId();

        form.clear();
        form.add("name", "second_list_with_default_flag_toggles_first_one2");
        form.add("defaultList", "on");
        String id2 = postListResource.post(form, HTML_VARIANT).getEntity().getId();

        TodoList findOne = listRepo.findOne(id1);
        TodoList findTwo = listRepo.findOne(id2);

        assertThat(findOne.isDefaultList(), is(false));
        assertThat(findTwo.isDefaultList(), is(true));
    }

}

