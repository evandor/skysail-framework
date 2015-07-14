package io.skysail.server.app.wiki.spaces.resources.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.server.app.wiki.*;
import io.skysail.server.app.wiki.spaces.Space;
import io.skysail.server.app.wiki.spaces.resources.SpaceResource;

import org.junit.*;
import org.mockito.Spy;
import org.restlet.data.Status;

public class GetSpaceResourceTest extends WikiResourceTestBase {

    @Spy
    private SpaceResource resource;

    @Before
    public void setUp() throws Exception {
        super.setUp(resource);
    }

    @Test
    public void retrieves_owners_entity() {
        Space space = RepositoryHelper.createTestSpace("admin");
        resource.getRequestAttributes().put("id", space.getId());
        resource.init(null, request, responses.get(resource.getClass().getName()));

        Space entity = resource.getEntity();

        assertThat(responses.get(resource.getClass().getName()).getStatus(),is(equalTo(Status.SUCCESS_OK)));
        assertThat(entity, is(not(nullValue())));
    }


    @Test
    public void doesnot_retrieve_other_users_entity() {
        Space space = RepositoryHelper.createTestSpace("demo");
        resource.getRequestAttributes().put("id", space.getId());
        resource.init(null, request, responses.get(resource.getClass().getName()));

        Space entity = resource.getEntity();

        assertThat(responses.get(resource.getClass().getName()).getStatus(),is(equalTo(Status.CLIENT_ERROR_FORBIDDEN)));
        assertThat(entity, is(nullValue()));
    }


}
