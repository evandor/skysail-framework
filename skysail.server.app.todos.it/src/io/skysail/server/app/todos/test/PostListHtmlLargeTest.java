package io.skysail.server.app.todos.test;

import io.skysail.client.testsupport.IntegrationTests;
import io.skysail.server.app.todos.TodoList;
import io.skysail.server.app.todos.test.browser.TodoListBrowser;

import org.junit.Test;
import org.restlet.data.MediaType;

public class PostListHtmlLargeTest extends IntegrationTests<TodoListBrowser, TodoList> {

    @Test
    public void postListResource_has_expected_Location() {
        createBrowser(MediaType.TEXT_HTML);
        browser.navigateToPostList();
        browser.verifyLocation("/Todos/v2/Lists/");
    }

    @Test
    public void postListResource_for_html_media_type_has_expected_LinkHeader() {
        createBrowser(MediaType.TEXT_HTML);
        browser.navigateToPostList();
        browser.verifyHeader("Link", "</Todos/v2>; rel=\"item\"; title=\"Todos\"; verbs=\"GET\"");
        browser.verifyHeader("Link", "</usermanagement>; rel=\"item\"; title=\"Usermanagement\"; verbs=\"GET\"");
        browser.verifyHeader("Link", "</plugins/v1>; rel=\"item\"; title=\"plugins\"; verbs=\"GET\"");
        browser.verifyHeaderCount("Link", 3);
    }

    @Test
    public void postListResource_for_json_media_type_has_expected_LinkHeader() {
        createBrowser(MediaType.APPLICATION_JSON);
        browser.navigateToPostList();
        browser.verifyHeader("Link", "</Todos/v2>; rel=\"item\"; title=\"Todos\"; verbs=\"GET\"");
        browser.verifyHeader("Link", "</usermanagement>; rel=\"item\"; title=\"Usermanagement\"; verbs=\"GET\"");
        browser.verifyHeader("Link", "</plugins/v1>; rel=\"item\"; title=\"plugins\"; verbs=\"GET\"");
        browser.verifyHeaderCount("Link", 3);
    }

    private void createBrowser(MediaType mediaType) {
        browser = new TodoListBrowser(mediaType, determinePort());
        browser.setUser("admin");
    }


}