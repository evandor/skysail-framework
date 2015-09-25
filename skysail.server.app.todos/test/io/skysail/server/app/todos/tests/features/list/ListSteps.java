package io.skysail.server.app.todos.tests.features.list;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.todos.TodoList;

import java.lang.reflect.Method;

import net.thucydides.core.annotations.Step;

import org.restlet.data.MediaType;
import org.restlet.engine.resource.VariantInfo;

public class ListSteps extends AbstractSteps {

    private ListTests listTests;
    private SkysailResponse<TodoList> result;

    @Step("posting form with '{0}'")
    public void post(String input) {
        result = listTests.postListresource.post(createForm(input),  new VariantInfo(MediaType.TEXT_HTML));
    }

    @Step("posting form with '{0}'")
    public void post(TodoList list) {
        result = listTests.postListresource.post(list, new VariantInfo(MediaType.APPLICATION_JSON));
    }

    @Step("yields name '{0}'")
    public void new_list_should_have_the_name(String name) {
        listTests.assertListResult(listTests.postListresource, result, name);
    }

    public void setTestClass(ListTests listTests) {
        this.listTests = listTests;
    }

    public void hasSingleValidationFailure(String key, String msg) {
        //listTests.assertSingleValidationFailure(listTests.postListresource, ( ConstraintViolationsResponse<?>)result,  key, msg);
    }

    @Step("asserting that '{0}' is not null")
    public void is_not_null(String key) throws Exception {
        TodoList entity = result.getEntity();
        Method getMethod = entity.getClass().getMethod("get" + key.substring(0,1).toUpperCase() + key.substring(1));
        assertThat(getMethod.invoke(entity), is(notNullValue()));
    }

}