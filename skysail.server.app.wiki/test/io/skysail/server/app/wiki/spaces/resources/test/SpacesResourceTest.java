package io.skysail.server.app.wiki.spaces.resources.test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertThat;
import io.skysail.server.app.wiki.spaces.Space;

import java.util.List;

import org.junit.Test;
import org.restlet.data.Status;


public class SpacesResourceTest extends AbstractSpaceResourceTest {

    @Test
    public void todoList_contains_created_todo_list() {
        Space aSpace = createSpace();

        init(spacesResource);
        List<Space> get = spacesResource.getEntity();

        assertThat(responses.get(spacesResource.getClass().getName()).getStatus(), is(equalTo(Status.SUCCESS_OK)));
        assertThat(get.size(), is(greaterThanOrEqualTo(1)));

        Space theSpace= get.stream().filter(list -> list.getName().equals(aSpace.getName())).findFirst()
                .orElseThrow(IllegalStateException::new);
        assertThat(theSpace.getName(), is(equalTo(aSpace.getName())));
    }

}
