package io.skysail.server.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.client.testsupport.*;
import io.skysail.server.app.todos.*;
import lombok.extern.slf4j.Slf4j;

import org.restlet.data.*;
import org.restlet.representation.Representation;
import org.restlet.util.Series;

import de.twenty11.skysail.server.domain.Credentials;

@Slf4j
public class SkysailServerBrowser extends ApplicationBrowser<SkysailServerBrowser, Credentials> {

    public SkysailServerBrowser(MediaType mediaType, String port) {
        super(TodoApplication.APP_NAME, mediaType, port);
    }

    public void navigateToPostList() {
        log.info("{}navigating to PostListResource", ApplicationClient.TESTTAG);
        login();
        //navigateToPostTodoListAs(client);
    }

    public String createTodoList(Credentials credentials) {
        log.info("{}creating todoList {}", ApplicationClient.TESTTAG, credentials);
        login();
        //navigateToPostTodoListAs(client);
        client.post(createForm(credentials));
        return client.getLocation().getLastSegment(true);
    }

//    public Representation getTodoLists() {
//        log.info("{}retrieving TodoLists", ApplicationClient.TESTTAG);
//        login();
//        return getTodoLists(client);
////        return client.getCurrentRepresentation();
//    }

    public void deleteTodoList(String id) {
        log.info("{}deleting TodoList #{}", ApplicationClient.TESTTAG, id);
        login();
        deleteTodoList(client, id);
    }

    public Representation getTodoList(String id) {
        log.info("{}retrieving TodoList #{}", ApplicationClient.TESTTAG, id);
        login();
        getTodoList(client, id);
        return client.getCurrentRepresentation();
    }

//    public void updateTodoList(TodoList theTodoList) {
//        log.info("{}updating TodoList #{}", ApplicationClient.TESTTAG, theTodoList.getId());
//        login();
//        updateTodoList(client, theTodoList);
//    }

    public void verifyLocation(String expectedLocationEnding) {
        Reference location = client.getLocation();
        assertThat(location.toString(), endsWith(expectedLocationEnding));
    }

    public void verifyHeader(String key, String containedValue) {
        Series<Header> currentHeader = client.getCurrentHeader();
        assertThat(currentHeader.getFirstValue(key), containsString(containedValue));
    }

    public void verifyHeaderCount(String key, int count) {
        Series<Header> currentHeader = client.getCurrentHeader();
        assertThat(currentHeader.getFirstValue(key).split(",").length, is(count));
    }

    @Override
    protected Form createForm(Credentials credentials) {
        Form form = new Form();
        form.add("username", credentials.getUsername());
        form.add("password", credentials.getPassword());
        return form;
    }

    private Representation getTodoLists(ApplicationClient<TodoList> client) {

        client.setUrl("/Todos/v2/Lists");
        return client.get();
//        ApplicationClient<TodoList> appRoot = client.gotoAppRoot();
//        System.out.println(appRoot.getLocation());
//        if (appRoot.getLocation().toString().endsWith("/Todos/v2/Lists/")) {
//
//
//        }
//        appRoot.followLinkTitle("Show Todo-Lists");
//            //.followLinkTitle(TodoApplication.APP_NAME);
    }

    private void updateTodoList(ApplicationClient<TodoList> client, TodoList theTodoList) {
        client.gotoAppRoot()
            .followLinkTitle("Show Todo-Lists")
            .followLinkTitleAndRefId("edit list", theTodoList.getId())
            .followLink(Method.PUT, theTodoList);
    }

    private void getTodoList(ApplicationClient<?> client, String id) {
        client.gotoAppRoot()
            .followLinkTitle("Show Todo-Lists");
            //.followLinkTitle(TodoApplication.APP_NAME);
    }

    private void deleteTodoList(ApplicationClient<?> client, String id) {
        client.gotoAppRoot() //
            .followLinkTitle("Show Todo-Lists")
                .followLinkTitleAndRefId("edit list", id)
                .followLink(Method.DELETE, null);
    }

    private void navigateToPostTodoListAs(ApplicationClient<TodoList> client) {
        ApplicationClient<TodoList> appClient = client.gotoAppRoot();
        if (appClient.getLocation().toString().endsWith("/Todos/v2/Lists/")) {
            // already redirected to PostListResource by server.
            client.setUrlFromCurrentRepresentation();
            return;
        }
        appClient.followLinkTitle("Show Todo-Lists")
            .followLinkTitle("create new List");
    }


}
