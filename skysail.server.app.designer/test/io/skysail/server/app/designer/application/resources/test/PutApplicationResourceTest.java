package io.skysail.server.app.designer.application.resources.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.Application;
import io.skysail.server.app.designer.application.resources.PutApplicationResource;
import io.skysail.server.app.designer.repo.DesignerRepository;
import io.skysail.server.testsupport.ResourceTestBase;

import java.util.HashMap;

import org.apache.shiro.subject.SimplePrincipalMap;
import org.junit.*;
import org.mockito.*;
import org.restlet.data.*;
import org.restlet.engine.resource.VariantInfo;

public class PutApplicationResourceTest extends ResourceTestBase {

    @Spy
    private PutApplicationResource resource;

    private DesignerRepository repo;

    @Before
    public void setUp() throws Exception {
        super.setUpFixture();
        super.setUpApplication(Mockito.mock(DesignerApplication.class));
        super.setUpResource(resource);

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
    public void empty_form_yields_validation_failure() {

        Application entity = new Application();
        entity.setName("application3");
        String id = DesignerRepository.add(entity).toString();

        form.add("name", "application3a");
        form.add("id", id);
        form.add("path", "../");
        form.add("packageName", "io.skysail.app.test");
        form.add("projectName", "testproj");

        resource.getRequestAttributes().put("id", id);
        resource.init(null, request, responses.get(resource.getClass().getName()));

        resource.put(form, new VariantInfo(MediaType.TEXT_HTML));

        Application entityFromDb = new DesignerRepository().getById(Application.class, id);

        assertThat(responses.get(resource.getClass().getName()).getStatus(),is(equalTo(Status.SUCCESS_OK)));
        assertThat(entityFromDb.getName(), is(equalTo("application3a")));
    }


}
