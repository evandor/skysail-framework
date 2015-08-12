package io.skysail.server.app.wiki.spaces.resources.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.server.app.wiki.*;
import io.skysail.server.app.wiki.repository.WikiRepository;
import io.skysail.server.app.wiki.spaces.*;
import io.skysail.server.app.wiki.spaces.resources.PutSpaceResource;

import org.junit.*;
import org.mockito.*;
import org.restlet.data.*;
import org.restlet.engine.resource.VariantInfo;

@Ignore
public class PutSpaceResourceTest extends WikiPostOrPutResourceTest {

    @Spy
    private PutSpaceResource resource;

    @Before
    public void setUp() throws Exception {
        super.setUpApplication(Mockito.mock(WikiApplication.class));
        super.setUpResource(resource);
        initRepository();
        initUser("admin");
        new UniquePerOwnerValidator().setDbService(testDb);
        resource.init(null, request, responses.get(resource.getClass().getName()));
    }

    @Test
    public void space_can_be_updated() {

        String spacename = "space_" + randomString();
        Space space = new Space(spacename);
        String id = WikiRepository.add(space).toString();

        form.add("name", spacename + "_NEW");
        form.add("id", id);
        resource.getRequestAttributes().put("id", id);
        resource.init(null, request, responses.get(resource.getClass().getName()));

        resource.put(form, new VariantInfo(MediaType.TEXT_HTML));

        Space listFromDb = new WikiRepository().getById(Space.class, id);

        assertThat(responses.get(resource.getClass().getName()).getStatus(),is(equalTo(Status.SUCCESS_OK)));
        assertThat(listFromDb.getName(), is(equalTo(spacename + "_NEW")));
    }

}