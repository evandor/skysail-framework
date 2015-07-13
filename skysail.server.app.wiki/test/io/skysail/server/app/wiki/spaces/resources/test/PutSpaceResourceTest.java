package io.skysail.server.app.wiki.spaces.resources.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.server.app.wiki.WikiApplication;
import io.skysail.server.app.wiki.WikiPostOrPutResourceTest;
import io.skysail.server.app.wiki.repository.WikiRepository;
import io.skysail.server.app.wiki.spaces.Space;
import io.skysail.server.app.wiki.spaces.UniquePerOwnerValidator;
import io.skysail.server.app.wiki.spaces.resources.PutSpaceResource;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.engine.resource.VariantInfo;

@Ignore
public class PutSpaceResourceTest extends WikiPostOrPutResourceTest {

    @Spy
    private PutSpaceResource resource;
    
    @Before
    public void setUp() throws Exception {
        super.setUp(Mockito.mock(WikiApplication.class));
        super.setUp(resource);
        initRepository();
        initUser("admin");
        new UniquePerOwnerValidator().setDbService(testDb);
        resource.init(null, request, response);
    }

    @Test
    public void space_can_be_updated() {
        
        String spacename = "space_" + randomString();
        Space space = new Space(spacename);
        String id = WikiRepository.add(space).toString();
        
        form.add("name", spacename + "_NEW");
        form.add("id", id);
        resource.getRequestAttributes().put("id", id);
        resource.init(null, request, response);
        
        resource.put(form, new VariantInfo(MediaType.TEXT_HTML));
        
        Space listFromDb = new WikiRepository().getById(Space.class, id);
        
        assertThat(response.getStatus(),is(equalTo(Status.SUCCESS_OK)));
        assertThat(listFromDb.getName(), is(equalTo(spacename + "_NEW")));
    }
    
}
