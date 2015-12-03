package io.skysail.server.app.designer.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.net.URL;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.osgi.framework.Bundle;
import org.osgi.service.component.ComponentContext;

import io.skysail.server.app.designer.ApplicationCreator;
import io.skysail.server.app.designer.application.Application;
import io.skysail.server.app.designer.model.CodegenApplicationModel;
import io.skysail.server.app.designer.repo.DesignerRepository;
import io.skysail.server.db.DbService;
import io.skysail.server.domain.core.Repositories;
import io.skysail.server.utils.BundleResourceReader;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationCreatorTest {

    private ApplicationCreator applicationCreator;

    @Mock
    private Repositories repos;

    @Mock
    private Bundle bundle;

    @Mock
    private DesignerRepository designerRepository;

    @Mock
    private DbService dbService;

    @Mock
    private ComponentContext componentContext;

    @Before
    public void setUp() throws Exception {
        when(bundle.getResource("/code")).thenReturn(new URL("file:///C:/git/skysail-framework/skysail.server.app.designer/resources/code"));
    }

    @Test
    public void createJavaApplication_from_DB_Application_Definition() {
        setupApplicationCreator(new Application("simpleApp", "io.skysail.simple", "generated", "projectName"));

        applicationCreator.create();
        
        CodegenApplicationModel applicationModel = applicationCreator.getApplicationModel();
        assertThat(applicationModel.getApplicationName(),is("simpleApp"));
        assertThat(applicationModel.getPackageName(),is("io.skysail.simple"));
    }
    
    @Test
    public void createInMemoryBundle_from_DB_Application_Definition() {
        setupApplicationCreator(new Application("simpleApp", "io.skysail.simple", "generated", "projectName"));

        applicationCreator.create();
        applicationCreator.setupInMemoryBundle(dbService, componentContext);
        
        CodegenApplicationModel applicationModel = applicationCreator.getApplicationModel();
        assertThat(applicationModel.getApplicationName(),is("simpleApp"));
        assertThat(applicationModel.getPackageName(),is("io.skysail.simple"));
    }

    private void setupApplicationCreator(Application application) {
        applicationCreator = new ApplicationCreator(application, designerRepository, repos, bundle);
        applicationCreator.setBundleResourceReader(new BundleResourceReader() {
            @Override
            public String readResource(Bundle bundle, String path) {
                return "C:/git/skysail-framework/skysail.server.app.designer/";
            }
        });
        applicationCreator.setJavaCompiler(new TestJavaCompiler());
    }
   


}
