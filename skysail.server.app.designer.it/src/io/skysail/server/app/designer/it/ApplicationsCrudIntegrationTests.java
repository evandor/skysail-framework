package io.skysail.server.app.designer.it;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.client.testsupport.IntegrationTests;
import io.skysail.server.app.designer.application.Application;
import io.skysail.server.app.designer.it.browser.ApplicationsBrowser;
import io.skysail.server.restlet.resources.SkysailServerResource;

import java.io.IOException;

import org.junit.*;
import org.osgi.framework.BundleException;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;

/**
 * Integration tests for creating, reading, updating, and deleting Applications.
 *
 */
@Ignore
public class ApplicationsCrudIntegrationTests extends IntegrationTests<ApplicationsBrowser, Application> {

    private Application entity;

    @Before
    public void setUp() {
        browser = new ApplicationsBrowser(MediaType.APPLICATION_JSON, determinePort());
        browser.setUser("admin");
        entity = browser.createRandomApplication();
    }

    @Test  // create and read
    public void creating_new_application_will_persist_it() throws Exception {
        browser.create(entity);
        String html = browser.getApplications().getText();
        assertThat(html, containsString(entity.getName()));
    }
    
    @Test // delete
    public void new_application_can_be_deleted() throws Exception {
        browser.create(entity);
        browser.deleteApplication(browser.getId());
        assertThat(browser.getApplications().getText(), not(containsString(entity.getName())));
    }

    @Test // update
    public void altering_application_updates_it_in_DB() throws Exception {
        browser.create(entity);
        assertThat(browser.getApplication(browser.getId()).getText(), containsString(entity.getName()));
        
        entity.setId(browser.getId());
        //entity.setDesc("description changed");
        entity.setName(entity.getName() + "_changed");
        browser.updateApplication(entity);
        
        String updatedText = browser.getApplication(browser.getId()).getText();
        assertThat(updatedText, containsString("_changed"));
    }

    @Test
    @Ignore // not working yet...
    public void stopping_and_starting_the_ServerBundle_doesnt_break_list_creationg() throws IOException, BundleException {
        stopAndStartBundle(SkysailServerResource.class);
        browser.create(entity);
        String html = browser.getApplications().getText();
        assertThat(html, containsString(entity.getName()));
    }
    
    

    @Test
    @Ignore
    public void posting_new_application_with_name_and_path_persists_it() throws Exception {
        Application application = new Application("app1");
       // application.setPath(".");
        browser.create(application);
        Representation applications = browser.getApplications();
        assertThat(applications.getText(), containsString("app1"));
    }
    
    @Test
    @Ignore
    public void posting_new_application_with_name_containing_specialChar_yields_validation_violation() throws Exception {
        thrown.expect(ResourceException.class);
        thrown.expectMessage("Bad Request");
        browser.create(new Application("app1!"));
    }
    
}
