package io.skysail.server.app.todos.tests.features.todos;

import io.skysail.server.app.todos.lists.test.AbstractListResourceTest;
import io.skysail.server.app.todos.tests.features.list.ListSteps;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.*;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;

@RunWith(SerenityRunner.class)
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
