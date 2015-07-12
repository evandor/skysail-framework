package io.skysail.server.app.designer.entities.resources.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.Application;
import io.skysail.server.app.designer.entities.resources.PostEntityResource;
import io.skysail.server.app.designer.repo.DesignerRepository;
import io.skysail.server.testsupport.ResourceTestBase;

import java.util.HashMap;

import org.apache.shiro.subject.SimplePrincipalMap;
import org.junit.*;
import org.mockito.*;
import org.restlet.data.*;
import org.restlet.engine.resource.VariantInfo;

public class PostEntityResourceTest extends ResourceTestBase {

   
    @Spy
    private PostEntityResource resource;

    @Before
    public void setUp() throws Exception {
        super.setUp(Mockito.mock(DesignerApplication.class), resource);

        DesignerRepository repo = new DesignerRepository();
        repo.setDbService(testDb);
        repo.activate();
        ((DesignerApplication)application).setDesignerRepository(repo);
        Mockito.when(((DesignerApplication)application).getRepository()).thenReturn(repo);
        
        Mockito.when(subjectUnderTest.getPrincipal()).thenReturn("admin");
        Mockito.when(subjectUnderTest.getPrincipals()).thenReturn(new SimplePrincipalMap(new HashMap<>()));
        setSubject(subjectUnderTest);
    }

    @Test
    public void valid_data_yields_new_entity() {
        
        Application application = new Application();
        application.setName("appForEntity1");
        String id = DesignerRepository.add(application).toString();
        
        resource.getRequestAttributes().put("id", id);
        resource.init(null, request, response);
        
        form.add("name", "entity1");
        resource.post(form, new VariantInfo(MediaType.TEXT_HTML));
        assertThat(response.getStatus(),is(equalTo(Status.SUCCESS_CREATED)));
    }


    
}
