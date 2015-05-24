package io.skysail.server.app.wiki.spaces.resources.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.server.app.wiki.WikiApplication;
import io.skysail.server.app.wiki.repository.WikiRepository;
import io.skysail.server.app.wiki.spaces.*;
import io.skysail.server.app.wiki.spaces.resources.PutSpaceResource;
import io.skysail.server.testsupport.PutResourceTest;

import java.util.HashMap;

import org.apache.shiro.subject.SimplePrincipalMap;
import org.junit.*;
import org.mockito.*;
import org.restlet.data.*;
import org.restlet.engine.resource.VariantInfo;

public class PutSpaceResourceTest extends PutResourceTest {

    @Spy
    private PutSpaceResource resource;
    
    private WikiRepository repo;

    @Before
    public void setUp() throws Exception {
        super.setUp(Mockito.mock(WikiApplication.class), resource);

        repo = new WikiRepository();
        repo.setDbService(testDb);
        repo.activate();
        ((WikiApplication)application).setWikiRepository(repo);
        Mockito.when(((WikiApplication)application).getRepository()).thenReturn(repo);
        
        Mockito.when(subjectUnderTest.getPrincipal()).thenReturn("admin");
        Mockito.when(subjectUnderTest.getPrincipals()).thenReturn(new SimplePrincipalMap(new HashMap<>()));
        setSubject(subjectUnderTest);
        
        new UniquePerOwnerValidator().setDbService(testDb);
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
