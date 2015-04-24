package io.skysail.server.app.designer.it;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import io.skysail.client.testsupport.IntegrationTests;
import io.skysail.server.app.designer.application.Application;
import io.skysail.server.app.designer.it.browser.ApplicationsBrowser;

import org.junit.Before;
import org.junit.Test;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;

/**
 * Integration tests for creating, reading, updating, and deleting Applications.
 *
 */
public class ApplicationsCrudIntegrationTests extends IntegrationTests<ApplicationsBrowser, Application> {

    @Before
    public void setUp() {
        browser = new ApplicationsBrowser(MediaType.APPLICATION_JSON, determinePort());
        browser.setUser("admin");
    }

    @Test
    public void posting_new_application_with_name_persists_it() throws Exception {
        browser.createApplication(new Application("app1"));
        Representation applications = browser.getApplications();
        assertThat(applications.getText(), containsString("app1"));
    }
    
    @Test
    public void posting_new_application_with_name_containing_specialChar_yields_validation_violation() throws Exception {
        thrown.expect(ResourceException.class);
        thrown.expectMessage("Bad Request");
        browser.createApplication(new Application("app1!"));
    }

}
