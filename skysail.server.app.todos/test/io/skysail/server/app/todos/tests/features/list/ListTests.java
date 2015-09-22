package io.skysail.server.app.todos.tests.features.list;

import io.skysail.server.app.todos.TodoList;
import io.skysail.server.app.todos.lists.test.AbstractListResourceTest;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.*;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;

@Narrative(text={
        "In order to manage todos",
        "As a user",
        "I want to be able to organize them in lists"})

@RunWith(SerenityRunner.class)
public class ListTests extends AbstractListResourceTest {

    @Steps
    ListSteps listSteps;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        listSteps.setTestClass(this);
        super.setUp();
    }

    @Test
    public void posting_empty_form_shouldGiven400() {
        listSteps.post("");
        listSteps.hasSingleValidationFailure("name", "may not be null");
    }

    @Test
    @Title("posting form should add new list with proper name")
    public void posting_form_urlencoded_shouldAddNewListWithGivenName() throws Exception {
        listSteps.post("name=theName");

        listSteps.new_list_should_have_the_name("theName");
        listSteps.is_not_null("created");
    }

    @Test
    @Title("posting json should add new list with proper name")
    public void posting_json_shouldAddNewListWithGivenName() {
        listSteps.post(new TodoList("todolist1"));
        listSteps.new_list_should_have_the_name("todolist1");
    }

    @Test
    @Ignore
    public void shouldNotChangeTheOwner() {
        listSteps.post("name=theName&owner=anotherOwner");
        listSteps.new_list_should_have_the_name("theName");
    }

}
