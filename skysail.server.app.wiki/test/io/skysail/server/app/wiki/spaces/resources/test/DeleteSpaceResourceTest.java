package io.skysail.server.app.wiki.spaces.resources.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.server.app.wiki.*;
import io.skysail.server.app.wiki.repository.WikiRepository;
import io.skysail.server.app.wiki.spaces.Space;
import io.skysail.server.app.wiki.spaces.resources.SpaceResource;

import org.junit.*;
import org.mockito.Spy;
import org.restlet.data.Status;

public class DeleteSpaceResourceTest extends WikiResourceTestBase {

    @Spy
    private SpaceResource resource;

    @Before
    public void setUp() throws Exception {
        super.setUp(resource);
    }

    @Test
    public void space_with_no_pages_can_be_deleted() {
        Space space = RepositoryHelper.createTestSpace("admin");
        resource.getRequestAttributes().put("id", space.getId());
        resource.init(null, request, responses.get(resource.getClass().getName()));

        resource.deleteEntity();

        Space spaceFromDb = new WikiRepository().getById(Space.class, space.getId());
        assertThat(responses.get(resource.getClass().getName()).getStatus(),is(equalTo(Status.SUCCESS_OK)));
        assertThat(spaceFromDb, is(nullValue()));
    }

    @Test
    public void space_with_page_cannot_be_deleted() {
        Space space = RepositoryHelper.createTestSpace("admin");
        RepositoryHelper.createTestPageIn(repo, space);
        resource.getRequestAttributes().put("id", space.getId());
        resource.init(null, request, responses.get(resource.getClass().getName()));

        resource.deleteEntity();

        Space spaceFromDb = new WikiRepository().getById(Space.class, space.getId());
        assertThat(responses.get(resource.getClass().getName()).getStatus(),is(equalTo(Status.SERVER_ERROR_INTERNAL)));
        assertThat(spaceFromDb, is(not(nullValue())));
    }


}
