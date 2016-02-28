
package io.skysail.api.responses.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;
import org.restlet.Response;

import io.skysail.api.responses.RelationTargetResponse;

public class RelationTargetResponseTest {

@Test
    public void object_is_initialized_with_response_and_entity() {
        AnEntity entity = new AnEntity();
        Response response = Mockito.mock(Response.class);
        List<AnEntity> thelist = Arrays.asList(entity);
        RelationTargetResponse<AnEntity> entityServerResponse = new RelationTargetResponse<>(response, thelist);
        assertThat(entityServerResponse.getEntity(),is(thelist));
        assertThat(entityServerResponse.getResponse(),is(response));
    }
}
