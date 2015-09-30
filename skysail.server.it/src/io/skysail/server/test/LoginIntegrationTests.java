package io.skysail.server.test;

import io.skysail.client.testsupport.*;

import java.math.BigInteger;

import org.junit.*;
import org.restlet.data.MediaType;

import de.twenty11.skysail.server.domain.Credentials;

/**
 * Integration tests for login.
 */
public class LoginIntegrationTests extends IntegrationTests<SkysailServerBrowser, Credentials> {

    private Credentials credentials;

    @Before
    public void setUp() {
        browser = new SkysailServerBrowser(MediaType.APPLICATION_JSON, determinePort());
        browser.setUser("admin");
        credentials = createRandomCredentials();
    }

    @Test
    public void login() throws Exception {
        ApplicationBrowser<SkysailServerBrowser, Credentials> login = browser.login();
        System.out.println(login);
       // createListAndCheckAssertions();
    }

//    @Test
//    // delete
//    @Ignore
//    public void new_todolist_can_be_deleted_by_owner() throws Exception {
//        String id = browser.createTodoList(todoList);
//        browser.deleteTodoList(id);
//        assertThat(browser.getTodoLists().getText(), not(containsString(todoList.getName())));
//    }
//
//    @Test
//    @Ignore
//    // cannot follow link as it is not displayed
//    public void new_todolist_cannot_be_deleted_by_someone_else() throws Exception {
//        String id = browser.createTodoList(todoList);
//        browser.setUser("demo");
//        browser.deleteTodoList(id);
//        assertThat(browser.getTodoLists().getText(), not(containsString(todoList.getName())));
//    }

//    @Test
//    // update
//    @Ignore
//    public void altering_todolist_updates_existing_todolist() throws Exception {
//        String id = browser.createTodoList(todoList);
//        assertThat(browser.getTodoList(id).getText(), containsString(todoList.getName()));
//
//        todoList.setId(id);
//        todoList.setDesc("description changed");
//        browser.updateTodoList(todoList);
//
//        String updatedText = browser.getTodoList(id).getText();
//        assertThat(updatedText, containsString("description changed"));
//    }
//
//    @Test
//    @Ignore
//    public void stopping_and_starting_the_TodosBundle_doesnt_break_list_creationg() throws IOException, BundleException {
//        stopAndStartBundle(TodoList.class);
//        createListAndCheckAssertions();
//    }

//    @Test
//    @Ignore
//    // not working yet...
//    public void stopping_and_starting_the_ServerBundle_doesnt_break_list_creationg() throws IOException,
//            BundleException {
//        stopAndStartBundle(SkysailServerResource.class);
//        createListAndCheckAssertions();
//    }

//    private void createListAndCheckAssertions() throws IOException {
//        browser.createTodoList(todoList);
//        String html = browser.getTodoLists().getText();
//        capture(html);
//        assertThat(html, containsString(todoList.getName()));
//    }

    private Credentials createRandomCredentials() {
        return new Credentials(new BigInteger(130, random).toString(32), "password");
    }
}
