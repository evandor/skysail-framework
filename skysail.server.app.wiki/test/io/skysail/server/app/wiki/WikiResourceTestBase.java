package io.skysail.server.app.wiki;

import io.skysail.server.app.wiki.repository.WikiRepository;
import io.skysail.server.app.wiki.spaces.UniquePerOwnerValidator;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.testsupport.ResourceTestBase;

import java.util.HashMap;

import org.apache.shiro.subject.SimplePrincipalMap;
import org.mockito.Mockito;

public abstract class WikiResourceTestBase extends ResourceTestBase {

    protected WikiRepository repo;

    public void setUp(SkysailServerResource<?> resource) throws Exception {
        super.setUpFixture();

        super.setUpApplication(Mockito.mock(WikiApplication.class));
        super.setUpResource(resource);

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


}
