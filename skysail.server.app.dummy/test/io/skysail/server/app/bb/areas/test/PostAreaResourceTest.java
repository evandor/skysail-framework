//package io.skysail.server.app.bb.areas.test;
//
//import static org.hamcrest.Matchers.is;
//import static org.junit.Assert.assertThat;
//
//import org.junit.Test;
//import org.restlet.data.Status;
//
//import io.skysail.api.responses.ConstraintViolationsResponse;
//import io.skysail.api.responses.SkysailResponse;
//import io.skysail.server.app.bb.AreaOld;
//import io.skysail.server.app.bb.test.AbstractAreaResourceTest;
//
//public class PostAreaResourceTest extends AbstractAreaResourceTest {
//
//    @Test
//    public void empty_form_data_yields_validation_failure() {
//        ConstraintViolationsResponse<?> post = (ConstraintViolationsResponse<?>) postAreaResource.post(form, HTML_VARIANT);
//        assertSingleValidationFailure(postAreaResource, post,  "title", "may not be null");
//    }
//
//    @Test
//    public void empty_json_data_yields_validation_failure() {
//        ConstraintViolationsResponse<?> post = (ConstraintViolationsResponse<?>) postAreaResource.post(new AreaOld(), JSON_VARIANT);
//        assertSingleValidationFailure(postAreaResource, post, "title", "may not be null");
//    }
//
//    @Test
//    public void valid_form_data_yields_new_entity() {
//        form.add("title", "aTitle");
//        SkysailResponse<AreaOld> result = postAreaResource.post(form, HTML_VARIANT);
//        assertListResult(postAreaResource, result, "aTitle");
//    }
//
//    @Test
//    public void valid_json_data_yields_new_entity() {
//        SkysailResponse<AreaOld> result = postAreaResource.post(new AreaOld("aTitle"), JSON_VARIANT);
//        assertThat(responses.get(postAreaResource.getClass().getName()).getStatus(),is(Status.SUCCESS_CREATED));
//        assertListResult(postAreaResource, result, "aTitle");
//    }
//
////    @Test
////    public void two_entries_with_same_name_yields_failure() {
////        form.add("name", "two_entries_with_same_name_yields_failure");
////        postListresource.post(form, new VariantInfo(MediaType.TEXT_HTML));
////        ConstraintViolationsResponse<?> post = (ConstraintViolationsResponse<?>) postListresource.post(form, HTML_VARIANT);
////        assertSingleValidationFailure(postListresource, post,  "", "name already exists");
////    }
////
////    @Test
////    public void new_list_with_default_flag_becomes_default() {
////        form.add("name", "new_list_with_default_flag_becomes_default");
////        form.add("defaultList", "on");
////        SkysailResponse<TodoList> result = postListresource.post(form, HTML_VARIANT);
////
////        assertThat(responses.get(postListresource.getClass().getName()).getStatus(),is(Status.SUCCESS_CREATED));
////        assertThat(result.getEntity().isDefaultList(),is(true));
////    }
////
////    @Test
////    public void users_first_list_becomes_default_even_if_not_flagged_as_such() {
////        setUpSubject("user_users_first_list_becomes_default_even_if_not_flagged_as_such");
////        form.add("name", "name_users_first_list_becomes_default_even_if_not_flagged_as_such");
////        SkysailResponse<TodoList> result = postListresource.post(form, HTML_VARIANT);
////
////        assertThat(responses.get(postListresource.getClass().getName()).getStatus(),is(Status.SUCCESS_CREATED));
////        assertThat(result.getEntity().isDefaultList(),is(true));
////    }
////
////    @Test
////    public void second_list_with_default_flag_toggles_first_one() {
////        setUpSubject("user_second_list_with_default_flag_toggles_first_one");
////
////        form.add("name", "second_list_with_default_flag_toggles_first_one1");
////        form.add("defaultList", "on");
////        String id1 = postListresource.post(form, HTML_VARIANT).getEntity().getId();
////
////        form.clear();
////        form.add("name", "second_list_with_default_flag_toggles_first_one2");
////        form.add("defaultList", "on");
////        String id2 = postListresource.post(form, HTML_VARIANT).getEntity().getId();
////
////        OrientVertex vertexById1 = ((List<OrientVertex>) new TodosRepository().getVertexById(TodoList.class, id1)).get(0);
////        OrientVertex vertexById2 = ((List<OrientVertex>) new TodosRepository().getVertexById(TodoList.class, id2)).get(0);
////
////        assertThat(vertexById1.getProperty("defaultList"), is(false));
////        assertThat(vertexById2.getProperty("defaultList"), is(true));
////    }
//
//}
