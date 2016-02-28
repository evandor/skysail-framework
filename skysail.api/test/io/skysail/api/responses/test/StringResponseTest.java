package io.skysail.api.responses.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.mockito.Mockito;
import org.restlet.Response;

import io.skysail.api.responses.StringResponse;

public class StringResponseTest {

    @Test
    public void object_is_initialized_with_response_and_entity() {
        AnEntity entity = new AnEntity();
        Response response = Mockito.mock(Response.class);
        StringResponse<AnEntity> entityServerResponse = new StringResponse<>(response, entity);
        assertThat(entityServerResponse.getEntity(),is(entity));
        assertThat(entityServerResponse.getResponse(),is(response));
    }
}
