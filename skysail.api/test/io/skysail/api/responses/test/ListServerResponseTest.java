package io.skysail.api.responses.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;
import org.restlet.Response;

import io.skysail.api.responses.ListServerResponse;

public class ListServerResponseTest {

    @Test
    public void object_is_initialized_with_response_and_entity() {
        AnEntity entity = new AnEntity();
        Response response = Mockito.mock(Response.class);
        List<AnEntity> theList = Arrays.asList(entity);
        ListServerResponse<AnEntity> entityServerResponse = new ListServerResponse<AnEntity>(response, theList);
        assertThat(entityServerResponse.getEntity(),is(theList));
        assertThat(entityServerResponse.getResponse(),is(response));
    }

}
