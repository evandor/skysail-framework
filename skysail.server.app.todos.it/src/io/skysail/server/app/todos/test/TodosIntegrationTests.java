package io.skysail.server.app.todos.test;

import static org.junit.Assert.assertTrue;
import io.skysail.api.documentation.DocumentationProvider;
import io.skysail.api.links.LinkRelation;
import io.skysail.client.testsupport.Client;
import io.skysail.client.testsupport.IntegrationTests;
import io.skysail.server.app.todos.TodoApplication;
import io.skysail.server.app.todos.todos.Todo;

import java.util.Collection;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.osgi.framework.ServiceReference;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.ServerResource;

public class TodosIntegrationTests extends IntegrationTests<TodosBrowser, Todo> {

    protected Representation createTodoListAs(Client client, String username, Form form) {
      navigateToPostTodoListAs(client, "admin");
      return client.post(form);
  }

  private void navigateToPostTodoListAs(Client client, String username) {
      client.loginAs(username, "skysail")
          .followLinkTitle(TodoApplication.APP_NAME)
          .followLinkRelation(LinkRelation.CREATE_FORM);
  }

  protected void getTodoListsFor(Client client, String username) {
      client.loginAs(username, "skysail")
          .followLinkTitle(TodoApplication.APP_NAME);
  }
    @Test
    public void documentationProvider_is_available() throws Exception {
        Collection<ServiceReference<DocumentationProvider>> serviceRefs = thisBundle.getBundleContext()
                .getServiceReferences(DocumentationProvider.class, null);
        assertTrue("service not available", serviceRefs.size() > 0);
    }

    @Test
    public void resourceMap_contains_apiResource() throws Exception {
        Collection<ServiceReference<DocumentationProvider>> serviceRefs = thisBundle.getBundleContext()
                .getServiceReferences(DocumentationProvider.class, null);
        DocumentationProvider documentationProvider = thisBundle.getBundleContext().getService(
                serviceRefs.iterator().next());
        Map<String, Class<? extends ServerResource>> resourceMap = documentationProvider.getResourceMap();
        assertTrue(resourceMap.size() > 0);
    }

//    @Test
//    public void get_html_on_ListsResource_returns_200() throws Exception {
//        Client client = new Client(MediaType.TEXT_HTML);
//        getTodoListsFor(client, "admin");
//        assertTrue(client.getCurrentRepresentation().getMediaType().getName().equals("text/html"));
//        assertTrue(client.getResponse().getStatus().getCode() == 200);
//    }
//
//    @Test
//    public void get_json_on_TodosResource_returns_200() throws Exception {
//        Client client = new Client(MediaType.APPLICATION_JSON);
//        getTodoListsFor(client, "admin");
//
//        assertTrue(client.getCurrentRepresentation().getMediaType().getName().equals("application/json"));
//        assertTrue(client.getResponse().getStatus().getCode() == 200);
//        String text = client.getCurrentRepresentation().getText();
//        assertTrue(text.startsWith("["));
//        assertTrue(text.endsWith("]"));
//    }

    @Test
    @Ignore
    public void post_html_with_missing_title_returns_badRequest() {
        Form form = new Form();
        form.add("title", "");
        thrown.expectMessage("Bad Request");
        //client.setUrl("/Todos/Todos/").post(form);
    }

//    @Test
//    @Ignore
//    public void post_json_with_missing_title_returns_badRequest() {
//        Client client = new Client(getBaseUrl(), MediaType.APPLICATION_JSON);
//
//        thrown.expectMessage("Bad Request");
//        Form form = new Form();
//        form.add("title", "");
//        client.setUrl("/Todos/Todos/").post(form);
//    }

    @Test
    @Ignore
    public void post_html_on_TodosResource_returns_200() {
        Form form = new Form();
        form.add("title", "mytitle");
        //client.setUrl("/Todos/Todos/").post(form);
        //assertTrue(client.getResponse().getStatus().getCode() == 200);

    }

    @Test
    @Ignore
    public void perfTest() throws Exception {
        Form form = new Form();
        form.add("title", "mytitle");
        for (int i = 0; i < 1; i++) {
            //client.setUrl("/TodoGen/Todos/").post(form);
        }
        for (int i = 0; i < 1; i++) {
            //client.setUrl("/TodoGen/Todos").get();
        }
    }

}
