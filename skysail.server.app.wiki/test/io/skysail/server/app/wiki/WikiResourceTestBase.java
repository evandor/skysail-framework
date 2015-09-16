package io.skysail.server.app.wiki;

import io.skysail.server.app.wiki.repository.SpacesRepository;
import io.skysail.server.app.wiki.spaces.UniquePerOwnerValidator;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.testsupport.ResourceTestBase;

import java.util.HashMap;

import org.apache.shiro.subject.SimplePrincipalMap;
import org.mockito.Mockito;

public abstract class WikiResourceTestBase extends ResourceTestBase {

    protected SpacesRepository spacesRepo;

    public void setUp(SkysailServerResource<?> resource) throws Exception {
        super.setUpFixture();

        super.setUpApplication(Mockito.mock(WikiApplication.class));
        super.setUpResource(resource);

        spacesRepo = new SpacesRepository();
        spacesRepo.setDbService(testDb);
        spacesRepo.activate();
        ((WikiApplication)application).setSpacesRepository(spacesRepo);
        Mockito.when(((WikiApplication)application).getSpacesRepo()).thenReturn(spacesRepo);

        Mockito.when(subjectUnderTest.getPrincipal()).thenReturn("admin");
        Mockito.when(subjectUnderTest.getPrincipals()).thenReturn(new SimplePrincipalMap(new HashMap<>()));
        setSubject(subjectUnderTest);

        new UniquePerOwnerValidator().setDbService(testDb);
    }


}
