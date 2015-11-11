package io.skysail.server.app.todos.tests.features.todos;

import io.skysail.server.app.todos.lists.test.AbstractListResourceTest;
import io.skysail.server.app.todos.tests.features.list.ListSteps;
import io.skysail.server.testsupport.categories.MediumTests;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.*;

import org.junit.*;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;

@Narrative(text={
        "In order to manage todos",
        "As a user",
        "I want to be able to organize them in lists"})

@RunWith(SerenityRunner.class)
@Category(MediumTests.class)
public class TodoTests extends AbstractListResourceTest {

    @Steps
    ListSteps listSteps;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        //listSteps.setTestClass(this);
        super.setUp();
    }

    @Test
    @Title("should add new todo")
    @Pending
    public void shouldAddNewTodoWithGivenTitle() {

    }

}
