package io.skysail.server.app.designer.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.util.Map;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.osgi.framework.Bundle;
import org.osgi.service.component.ComponentContext;

import com.fasterxml.jackson.dataformat.yaml.snakeyaml.Yaml;

import io.skysail.server.app.designer.ApplicationCreator;
import io.skysail.server.app.designer.application.Application;
import io.skysail.server.app.designer.model.CodegenApplicationModel;
import io.skysail.server.app.designer.repo.DesignerRepository;
import io.skysail.server.db.DbService;
import io.skysail.server.domain.core.Repositories;
import io.skysail.server.utils.BundleResourceReader;

@SuppressWarnings("restriction")
@RunWith(MockitoJUnitRunner.class)
public class ApplicationCreatorTest {

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
    
    Yaml yaml = new Yaml();

    @Before
    public void setUp() throws Exception {
        String currentDir = Paths.get(".", "resources", "code").toAbsolutePath().toString();
        when(bundle.getResource("/code")).thenReturn(new URL("file:///" + currentDir));
    }

    @Test
    public void creates_ApplicationModel_from_DB_Application_Definition() {
        ApplicationCreator applicationCreator = setupApplicationCreator(new Application("simpleApp", "io.skysail.simple", "generated", "projectName"));

        applicationCreator.createApplication(dbService, componentContext);

        CodegenApplicationModel applicationModel = applicationCreator.getApplicationModel();
        assertThat(applicationModel.getApplicationName(), is("simpleApp"));
        assertThat(applicationModel.getPackageName(), is("io.skysail.simple"));
    }

    @Test
    public void creates_InMemoryBundle_from_DB_Application_Definition() throws IOException {
        ApplicationCreator applicationCreator = setupApplicationCreator(readApplicationFromYamlFile("simpleApplication.yml"));
        applicationCreator.createApplication(dbService, componentContext);

       // CodegenApplicationModel applicationModel = applicationCreator.getApplicationModel();
    }

    
    @Test
    public void creates_InMemoryBundle_from_DB_Application_Definition2() throws IOException {
        ApplicationCreator applicationCreator = setupApplicationCreator(readApplicationFromYamlFile("checklist.yml"));
        applicationCreator.createApplication(dbService, componentContext);

       // CodegenApplicationModel applicationModel = applicationCreator.getApplicationModel();
    }

    @SuppressWarnings({ "unchecked" })
    private Application readApplicationFromYamlFile(String testfile) throws IOException {
        Path path = Paths.get("resources", "testinput", testfile);
        StringBuilder sb = new StringBuilder();
        Files.lines(path).forEach(line -> sb.append(line).append("\n"));
        Map<String,Object> list = (Map<String,Object>) yaml.load(sb.toString());
        Map<String,Object> appFromYml = (Map<String, Object>) list.get("Application");
        return new Application((String)appFromYml.get("name"), (String)appFromYml.get("packageName"), (String)appFromYml.get("path"), (String)appFromYml.get("projectName"));
    }

    private ApplicationCreator setupApplicationCreator(Application application) {
        applicationCreator = new ApplicationCreator(application, designerRepository, repos, bundle);
        applicationCreator.setBundleResourceReader(new BundleResourceReader() {
            @Override
            public String readResource(Bundle bundle, String path) {
                return "C:/git/skysail-framework/skysail.server.app.designer/";
            }
        });
        applicationCreator.setJavaCompiler(new TestJavaCompiler());
        return applicationCreator;
    }

}
