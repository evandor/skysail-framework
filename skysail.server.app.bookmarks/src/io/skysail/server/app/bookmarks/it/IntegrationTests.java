package io.skysail.server.app.bookmarks.it;

import io.skysail.api.links.LinkRelation;
import io.skysail.client.testsupport.Client;
import io.skysail.server.app.bookmarks.app.BookmarksApplication;
import lombok.extern.slf4j.Slf4j;

import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.restlet.data.Form;
import org.restlet.representation.Representation;

@Slf4j
public class IntegrationTests {

    private static final String HOST = "http://localhost";
    private static final String PORT = "2015";

    protected Bundle thisBundle = FrameworkUtil.getBundle(this.getClass());

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    protected Browser browser;

    @Rule
    public TestRule watcher = new TestWatcher() {
        @Override
        protected void starting(Description description) {
            log.info("");
            log.info("--------------------------------------------");
            log.info("{}running test '{}'", Client.TESTTAG, description.getMethodName());
            log.info("--------------------------------------------");
            log.info("");
        }
    };

    protected String getBaseUrl() {
        return HOST + (PORT != null ? ":" + PORT : "");
    }

    protected Representation createTodoListAs(Client client, String username, Form form) {
        navigateToPostTodoListAs(client, "admin");
        return client.post(form);
    }

    private void navigateToPostTodoListAs(Client client, String username) {
        client.loginAs(username, "skysail").followLinkTitle(BookmarksApplication.APP_NAME)
                .followLinkRelation(LinkRelation.CREATE_FORM);
    }

    protected void getTodoListsFor(Client client, String username) {
        client.loginAs(username, "skysail").followLinkTitle(BookmarksApplication.APP_NAME);
    }

}
