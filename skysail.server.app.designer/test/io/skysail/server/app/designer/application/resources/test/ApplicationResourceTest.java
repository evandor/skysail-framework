package io.skysail.server.app.designer.application.resources.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.Application;
import io.skysail.server.app.designer.application.resources.*;
import io.skysail.server.app.designer.repo.DesignerRepository;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.testsupport.ResourceTestBase;

import java.util.HashMap;

import org.apache.shiro.subject.SimplePrincipalMap;
import org.junit.Before;
import org.mockito.*;
import org.restlet.data.Status;

public abstract class ApplicationResourceTest extends ResourceTestBase {

    @Spy
    protected PostApplicationResource postApplicationResource;

    @Spy
    protected PutApplicationResource putApplicationResource;

    @Spy
    protected ApplicationsResource applicationsResource;

    @Spy
    protected ApplicationResource applicationResource;

    protected DesignerRepository repo;

    @Before
    public void setUp() throws Exception {
        super.setUpFixture();

        super.setUpApplication(Mockito.mock(DesignerApplication.class));
        super.setUpResource(applicationResource);
        super.setUpResource(applicationsResource);
        super.setUpResource(putApplicationResource);
        super.setUpResource(postApplicationResource);
        setUpRepository(new DesignerRepository());
        setUpSubject("admin");

       // new UniquePerOwnerValidator().setDbService(testDb);
    }

    protected void assertListResult(SkysailServerResource<?> resource, SkysailResponse<Application> result, String name) {
        Application entity = result.getEntity();
        assertThat(responses.get(resource.getClass().getName()).getStatus(),is(equalTo(Status.SUCCESS_CREATED)));
        assertThat(entity.getName(),is(equalTo(name)));
        assertThat(entity.getPackageName(),is(equalTo("io.skysail.testpackage")));
        assertThat(entity.getPath(),is(equalTo("../")));
        assertThat(entity.getProjectName(),is(equalTo("TestProject")));
        assertThat(entity.getOwner(),is("admin"));
    }

    public void setUpRepository(DesignerRepository todosRepository) {
        repo = todosRepository;
        repo.setDbService(testDb);
        repo.activate();
        ((DesignerApplication)application).setDesignerRepository(repo);
        Mockito.when(((DesignerApplication)application).getRepository()).thenReturn(repo);

    }

    public void setUpSubject(String owner) {
        Mockito.when(subjectUnderTest.getPrincipal()).thenReturn(owner);
        Mockito.when(subjectUnderTest.getPrincipals()).thenReturn(new SimplePrincipalMap(new HashMap<>()));
        setSubject(subjectUnderTest);
    }

    protected Application createApplication() {
        Application aList = new Application();
        aList.setName("list_" + randomString());
        SkysailResponse<Application> post = postApplicationResource.post(aList,JSON_VARIANT);
        getAttributes().clear();

        return post.getEntity();
    }

    protected void init(SkysailServerResource<?> resource) {
        resource.init(null, request, responses.get(resource.getClass().getName()));
    }

    protected void setAttributes(String name, String id) {
        getAttributes().clear();
        getAttributes().put(name, id);
    }


}
