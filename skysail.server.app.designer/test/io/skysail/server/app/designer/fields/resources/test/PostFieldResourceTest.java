package io.skysail.server.app.designer.fields.resources.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.Application;
import io.skysail.server.app.designer.entities.Entity;
import io.skysail.server.app.designer.fields.resources.PostFieldResource;
import io.skysail.server.app.designer.repo.DesignerRepository;
import io.skysail.server.testsupport.PostResourceTest;

import java.util.HashMap;

import org.apache.shiro.subject.SimplePrincipalMap;
import org.junit.*;
import org.mockito.*;
import org.restlet.data.*;
import org.restlet.engine.resource.VariantInfo;

public class PostFieldResourceTest extends PostResourceTest {

   
    @Spy
    private PostFieldResource resource;
    
    private DesignerRepository repo;

    @Before
    public void setUp() throws Exception {
        super.setUp(Mockito.mock(DesignerApplication.class), resource);

        repo = new DesignerRepository();
        repo.setDbService(testDb);
        repo.activate();
        ((DesignerApplication)application).setDesignerRepository(repo);
        Mockito.when(((DesignerApplication)application).getRepository()).thenReturn(repo);
        
        Mockito.when(subjectUnderTest.getPrincipal()).thenReturn("admin");
        Mockito.when(subjectUnderTest.getPrincipals()).thenReturn(new SimplePrincipalMap(new HashMap<>()));
        setSubject(subjectUnderTest);
    }

    @Test
    @Ignore // TODO
    public void valid_data_yields_new_entity() {
        
        Entity entity = new Entity() {{
            setName("namefield");
        }};
        
        Application application = new Application();
        application.setName("appForEntity2");
        //application.setEntities(Arrays.asList(entity));
        String id = DesignerRepository.add(application).toString();
        
        application = repo.getById(Application.class, id);
        application.getEntities().add(entity);
        repo.update(application);
        
        resource.getRequestAttributes().put("id", id);
        resource.init(null, request, response);
        
        form.add("name", "entity1");
        resource.post(form, new VariantInfo(MediaType.TEXT_HTML));
        assertThat(response.getStatus(),is(equalTo(Status.SUCCESS_CREATED)));
    }


    
}