package io.skysail.server.app.wiki.spaces.resources.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.server.app.wiki.*;
import io.skysail.server.app.wiki.spaces.Space;
import io.skysail.server.app.wiki.spaces.resources.SpacesResource;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.*;
import org.mockito.Spy;
import org.restlet.data.Status;

public class GetSpacesResourceTest extends WikiResourceTestBase {

    @Spy
    private SpacesResource resource;
    
    @Before
    public void setUp() throws Exception {
        super.setUp(resource);
    }

    @Test
    public void retrieves_owners_entity() {
        Space space = RepositoryHelper.createTestSpace("admin");
        resource.getRequestAttributes().put("id", space.getId());
        resource.init(null, request, response);
        
        List<Space> entities = resource.getEntity();

        assertThat(response.getStatus(),is(equalTo(Status.SUCCESS_OK)));
        List<String> spaceNames = entities.stream().map(Space::getName).collect(Collectors.toList());
        assertThat(spaceNames, hasItem(space.getName()));
    }
    
    @Test
    @Ignore
    public void doesnot_retrieve_other_users_entity() {
        Space space = RepositoryHelper.createTestSpace("demo");
        resource.getRequestAttributes().put("id", space.getId());
        resource.init(null, request, response);
        
        List<Space> entities = resource.getEntity();

        assertThat(response.getStatus(),is(equalTo(Status.SUCCESS_OK)));
        List<String> spaceNames = entities.stream().map(Space::getName).collect(Collectors.toList());
        assertThat(spaceNames, not(hasItem(space.getName())));
    }


}
