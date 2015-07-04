package io.skysail.server.app.designer.test;

import io.skysail.server.app.designer.ApplicationCreator;
import io.skysail.server.app.designer.application.Application;
import io.skysail.server.app.designer.repo.DesignerRepository;

import org.junit.Before;
import org.mockito.Mockito;
import org.osgi.framework.Bundle;

import de.twenty11.skysail.server.core.restlet.SkysailRouter;

public class ApplicationCreatorTest {

    private ApplicationCreator applicationCreator;

    @Before
    public void setUp() throws Exception {
        Application application = new Application("testapp");
        SkysailRouter router = Mockito.mock(SkysailRouter.class);
        DesignerRepository repo = Mockito.mock(DesignerRepository.class);
        Bundle bundle = Mockito.mock(Bundle.class);
        applicationCreator = new ApplicationCreator(application, router, repo, bundle);
    }
    
//    @Test
//    public void testName() throws Exception {
//        boolean created = applicationCreator.create(null);
//    }

}
