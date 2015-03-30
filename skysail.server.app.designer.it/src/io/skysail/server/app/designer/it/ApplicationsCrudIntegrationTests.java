package io.skysail.server.app.designer.it;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import io.skysail.client.testsupport.Client;
import io.skysail.server.app.designer.application.Application;
import lombok.extern.slf4j.Slf4j;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.restlet.data.Reference;

/**
 * Integration tests for creating, reading, updating, and deleting TodoLists.
 *
 */
@Slf4j
public class ApplicationsCrudIntegrationTests extends IntegrationTests {

    private Browser browser;

    @Rule
    public TestRule watcher = new TestWatcher() {
       protected void starting(Description description) {
    	   log.info("");
    	   log.info("--------------------------------------------");
    	   log.info("{}running test '{}'", Client.TESTTAG, description.getMethodName());
    	   log.info("--------------------------------------------");
    	   log.info("");
       }
    };
    
    @Before
    public void setUp() {
        browser = new Browser(getBaseUrl());
    }

    @Test
    public void creating_application_persists_new_application() throws Exception {
        browser.asUser("admin").createApplication(new Application("app1"));
        String html = browser.asUser("admin").getApplications().getText();
        assertThat(html, containsString("app1"));
    }
    
    @Test
    public void new_application_can_be_deleted_by_owner() throws Exception {
        Reference location = browser.asUser("admin").createApplication(new Application("app2"));
        String id = location.getLastSegment();
        browser.asUser("admin").deleteApplication(id);
        String html = browser.asUser("admin").getApplications().getText();
        assertThat(html, not(containsString("app1")));
    }

}
