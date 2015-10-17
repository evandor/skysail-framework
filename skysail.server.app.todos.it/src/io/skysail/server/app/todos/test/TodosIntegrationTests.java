package io.skysail.server.app.todos.test;

import io.skysail.api.links.LinkRelation;
import io.skysail.client.testsupport.*;
import io.skysail.server.app.todos.TodoApplication;
import io.skysail.server.app.todos.test.browser.TodosBrowser;
import io.skysail.server.app.todos.todos.Todo;

import org.junit.*;
import org.restlet.data.Form;
import org.restlet.representation.Representation;

public class TodosIntegrationTests extends BrowserTests<TodosBrowser, Todo> {

    protected Representation createTodoListAs(ApplicationClient client, String username, Form form) {
        navigateToPostTodoListAs(client, "admin");
        return client.post(form);
    }

    private void navigateToPostTodoListAs(ApplicationClient client, String username) {
        client.loginAs(username, "skysail").followLinkTitle(TodoApplication.APP_NAME)
                .followLinkRelation(LinkRelation.CREATE_FORM);
    }

    protected void getTodoListsFor(ApplicationClient client, String username) {
        client.loginAs(username, "skysail").followLinkTitle(TodoApplication.APP_NAME);
    }


    // @Test
    // public void get_html_on_ListsResource_returns_200() throws Exception {
    // ApplicationClient client = new ApplicationClient(MediaType.TEXT_HTML);
    // getTodoListsFor(client, "admin");
    // assertTrue(client.getCurrentRepresentation().getMediaType().getName().equals("text/html"));
    // assertTrue(client.getResponse().getStatus().getCode() == 200);
    // }
    //
    // @Test
    // public void get_json_on_TodosResource_returns_200() throws Exception {
    // ApplicationClient client = new
    // ApplicationClient(MediaType.APPLICATION_JSON);
    // getTodoListsFor(client, "admin");
    //
    // assertTrue(client.getCurrentRepresentation().getMediaType().getName().equals("application/json"));
    // assertTrue(client.getResponse().getStatus().getCode() == 200);
    // String text = client.getCurrentRepresentation().getText();
    // assertTrue(text.startsWith("["));
    // assertTrue(text.endsWith("]"));
    // }

    @Test
    @Ignore
    public void post_html_with_missing_title_returns_badRequest() {
        Form form = new Form();
        form.add("title", "");
        thrown.expectMessage("Bad Request");
        // client.setUrl("/Todos/Todos/").post(form);
    }

    // @Test
    // @Ignore
    // public void post_json_with_missing_title_returns_badRequest() {
    // ApplicationClient client = new ApplicationClient(getBaseUrl(),
    // MediaType.APPLICATION_JSON);
    //
    // thrown.expectMessage("Bad Request");
    // AForm form = new AForm();
    // form.add("title", "");
    // client.setUrl("/Todos/Todos/").post(form);
    // }

    @Test
    @Ignore
    public void post_html_on_TodosResource_returns_200() {
        Form form = new Form();
        form.add("title", "mytitle");
        // client.setUrl("/Todos/Todos/").post(form);
        // assertTrue(client.getResponse().getStatus().getCode() == 200);

    }

    @Test
    @Ignore
    public void perfTest() throws Exception {
        Form form = new Form();
        form.add("title", "mytitle");
        for (int i = 0; i < 1; i++) {
            // client.setUrl("/TodoGen/Todos/").post(form);
        }
        for (int i = 0; i < 1; i++) {
            // client.setUrl("/TodoGen/Todos").get();
        }
    }

}
