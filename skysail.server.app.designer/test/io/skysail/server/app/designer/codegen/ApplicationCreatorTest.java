package io.skysail.server.app.designer.codegen;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Dictionary;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;

import de.twenty11.skysail.server.app.ApplicationProvider;
import io.skysail.domain.core.Repositories;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.model.DesignerApplicationModel;
import io.skysail.server.app.designer.repo.DesignerRepository;
import io.skysail.server.app.designer.test.TestJavaCompiler;
import io.skysail.server.app.designer.test.utils.YamlTestFileReader;
import io.skysail.server.db.DbService;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.utils.BundleResourceReader;
import lombok.extern.slf4j.Slf4j;

@RunWith(MockitoJUnitRunner.class)
@Slf4j
public class ApplicationCreatorTest {

    @Mock
    private Repositories reposMock;

    @Mock
    private Bundle bundleMock;

    @Mock
    private BundleContext bundleContextMock;

    @Mock
    private DesignerRepository designerRepositoryMock;

    @Mock
    private DbService dbServiceMock;

    @Mock
    private ComponentContext componentContextMock;
    
    @Spy
    private JavaCompiler javaCompilerSpy = new TestJavaCompiler();

    @Before
    public void setUp() throws Exception {
        String currentCodeDir = Paths.get("resources", "code").toAbsolutePath().toString();
        when(bundleMock.getResource("/code")).thenReturn(new URL("file:///" + currentCodeDir));

        String currentOsgiInfDir = Paths.get("resources", "code", "OSGI-INF").toAbsolutePath().toString();
        when(bundleMock.getResource("/code/OSGI-INF")).thenReturn(new URL("file:///" + currentOsgiInfDir));

        when(bundleMock.getBundleContext()).thenReturn(bundleContextMock);
        
        when(bundleContextMock.getBundles()).thenReturn(new Bundle[0]);
    }

    @Test
    public void creates_InMemoryBundle_from_empty_application() throws IOException {
        ApplicationCreator applicationCreator = setupApplicationCreator(YamlTestFileReader.read("empty.yml"));

        applicationCreator.createApplication();

        verifyCreatedApplication(applicationCreator);
        Collection<String> repositoryIds = applicationCreator.getApplicationModel().getRepositoryIds();
        assertThat(repositoryIds.size(),is(0));
    }

    @Test
    @Ignore // FIXME
    public void creates_InMemoryBundle_from_application_with_one_entity() throws IOException {
        ApplicationCreator applicationCreator = setupApplicationCreator(YamlTestFileReader.read("transactions.yml"));

        applicationCreator.createApplication();

        verifyCreatedApplication(applicationCreator);
        Collection<String> repositoryIds = applicationCreator.getApplicationModel().getRepositoryIds();
        assertThat(repositoryIds.size(),is(0));
    }

    @Test
    @Ignore // FIXME
    public void creates_InMemoryBundle_from_DB_Application_Definition2() throws IOException {
        ApplicationCreator applicationCreator = setupApplicationCreator(YamlTestFileReader.read("checklist.yml"));
        applicationCreator.createApplication();

       // DesignerApplicationModel applicationModel = applicationCreator.getApplicationModel();
    }

    private void verifyCreatedApplication(ApplicationCreator applicationCreator) {
        verifyProjectFilesExist(applicationCreator.getApplicationModel());
        verifyJavaCompilerCalls();
        verifyJavaFilesExist(applicationCreator.getApplicationModel());
       // verifyApplicationServiceWasRegistered();
    }
    
    private void verifyJavaCompilerCalls() {
        verify(javaCompilerSpy, times(1)).reset();
    }
    
    private ApplicationCreator setupApplicationCreator(DbApplication application) {
        ApplicationCreator applicationCreator = new ApplicationCreator(application, bundleMock);
        applicationCreator.setBundleResourceReader(new BundleResourceReader() {
            @Override
            public String readResource(Bundle bundle, String path) {
                return "C:/git/skysail-framework/skysail.server.app.designer/";
            }
        });
        applicationCreator.setJavaCompiler(javaCompilerSpy);
        return applicationCreator;
    }
    
    @SuppressWarnings("unchecked")
    private void verifyApplicationServiceWasRegistered() {
        verify(bundleContextMock).registerService(
                org.mockito.Matchers.argThat(new ArgumentMatcher<String[]>() {
                    @Override
                    public boolean matches(Object argument) {
                        String[] actualArgument = (String[]) argument;
                        return actualArgument[0].equals(ApplicationProvider.class.getName()) &&
                               actualArgument[1].equals(MenuItemProvider.class.getName());
                    }
                    
                }),
                org.mockito.Matchers.argThat(new ArgumentMatcher<Object>() {

                    @Override
                    public boolean matches(Object argument) {
                        return argument instanceof SkysailApplication;
                    }

                }), isNull(Dictionary.class));
    }
    
    private void verifyJavaFilesExist(DesignerApplicationModel appModel) {
        String projectPath = "generated/" + appModel.getName() + "/" + appModel.getProjectName() + "/";
        String applicationPath = projectPath + "src-gen/" + appModel.getPackageName().replace(".", "/") + "/";
        assertFileExists(applicationPath, appModel.getName() + "Application.java");
    }

    private void verifyProjectFilesExist(DesignerApplicationModel appModel) {
        // "generated/empty/skysail.server.designer.empty"
        String projectPath = "generated/" + appModel.getName() + "/" + appModel.getProjectName() + "/";
        
        assertFileExists(projectPath, ".project");
        assertFileExists(projectPath, ".classpath");
        assertFileExists(projectPath, "bnd.bnd");
        //assertFileExists(path, "bnd.bndrun");
        assertFileExists(projectPath, "build.gradle");
        assertFileExists(projectPath, "resources/.gitignore");
        assertFileExists(projectPath, "config/local/logback.xml");
        assertFileExists(projectPath, "config/local/io.skysail.server.db.DbConfigurations-skysailgraph.cfg");
    }

    // FIXME
    private void assertFileExists(String path, String filename) {
        File file = Paths.get(path + "/" + filename).toFile();
        log.debug("checking if file {} exists...", file.getAbsolutePath());
       // assertThat(file.exists(), is(true));
    }
}
