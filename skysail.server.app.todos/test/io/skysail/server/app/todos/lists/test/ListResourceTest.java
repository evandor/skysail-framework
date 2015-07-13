package io.skysail.server.app.todos.lists.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.todos.TodoList;
import io.skysail.server.app.todos.lists.PostListResource;
import io.skysail.server.app.todos.lists.PutListResource;
import io.skysail.server.testsupport.ResourceTestBase;

import org.mockito.Spy;
import org.restlet.data.Status;

public abstract class ListResourceTest extends ResourceTestBase {

    @Spy
    protected PostListResource postListresource;
    @Spy
    protected PutListResource putListResource;
    
    protected void assertResult(SkysailResponse<TodoList> result, String name) {
        TodoList entity = result.getEntity();
        assertThat(response.getStatus(),is(equalTo(Status.SUCCESS_CREATED)));
        assertThat(entity.getName(),is(equalTo(name)));
        assertThat(entity.getCreated(),is(not(nullValue())));
        assertThat(entity.getModified(),is(nullValue()));
        assertThat(entity.getOwner(),is("admin"));
        //assertThat(result.getEntity().getTodosCount(),is(0));
    }

    
}
