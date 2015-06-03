package io.skysail.server.app.designer.it;

import io.skysail.client.testsupport.IntegrationTests;
import io.skysail.server.app.designer.application.Application;
import io.skysail.server.app.designer.it.browser.ApplicationsBrowser;

import org.junit.Before;
import org.junit.Test;
import org.restlet.data.MediaType;

/**
 * Integration tests for creating, reading, updating, and deleting Applications.
 *
 */
public class ReferenceApplicationIntegrationTests extends IntegrationTests<ApplicationsBrowser, Application> {

    private Application entity;

    @Before
    public void setUp() {
        browser = new ApplicationsBrowser(MediaType.APPLICATION_JSON, determinePort());
        browser.setUser("admin");
        entity = createReferenceApplication();
    }


    @Test  
    public void creating_new_application_will_persist_it() throws Exception {
        //createListAndCheckAssertions();
    }
    
    private Application createReferenceApplication() {
        return new Application("CampaignManager");
    }
   
}
