//package io.skysail.server.app.todos.test;
//
//import static org.hamcrest.CoreMatchers.containsString;
//import static org.hamcrest.CoreMatchers.not;
//import static org.junit.Assert.assertThat;
//import io.skysail.client.testsupport.ApplicationClient;
//import io.skysail.client.testsupport.IntegrationTests;
//
//import java.io.IOException;
//
//import org.junit.Test;
//import org.restlet.data.Form;
//import org.restlet.data.MediaType;
//
//public class AccessTests extends IntegrationTests<TodosBrowser> {
//
//    @Test
//    public void demoUser_doesnot_see_admins_todolists_inHtml() throws IOException {
//        ApplicationClient client = new ApplicationClient(getBaseUrl(), MediaType.TEXT_HTML);
//
//        Form form = new Form();
//        form.add("name", "adminstodolist");
//        createTodoListAs(client, "admin", form);
//
//        getTodoListsFor(client, "demo");
//
//        String html = client.getCurrentRepresentation().getText();
//        assertThat(html, not(containsString("adminstodolist")));
//    }
//
//    @Test
//    public void demoUser_doesnot_see_admins_todolists_inJson() throws IOException {
//        ApplicationClient client = new ApplicationClient(getBaseUrl(), MediaType.APPLICATION_JSON);
//
//        Form form = new Form();
//        form.add("name", "adminstodolist");
//        createTodoListAs(client, "admin", form);
//
//        getTodoListsFor(client, "demo");
//
//        String html = client.getCurrentRepresentation().getText();
//        assertThat(html, not(containsString("adminstodolist")));
//    }
//}
