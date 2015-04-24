//package io.skysail.server.app.designer.it;
//
//import org.junit.Rule;
//import org.junit.rules.ExpectedException;
//import org.osgi.framework.Bundle;
//import org.osgi.framework.FrameworkUtil;
//
//public class IntegrationTests {
//
//    private static final String HOST = "http://localhost";
//    private static final String PORT = "2015";
//
//    protected Bundle thisBundle = FrameworkUtil.getBundle(this.getClass());
//
//    @Rule
//    public ExpectedException thrown = ExpectedException.none();
//
//    protected String getBaseUrl() {
//        return HOST + (PORT != null ? ":" + PORT : "");
//    }
//
////    protected Representation createTodoListAs(ApplicationClient client, String username, Form form) {
////        navigateToPostTodoListAs(client, "admin");
////        return client.post(form);
////    }
//
////    private void navigateToPostTodoListAs(ApplicationClient client, String username) {
////        client.loginAs(username, "skysail").followLinkTitle(TodoApplication.APP_NAME)
////                .followLinkRelation(LinkRelation.CREATE_FORM);
////    }
////
////    protected void getTodoListsFor(ApplicationClient client, String username) {
////        client.loginAs(username, "skysail").followLinkTitle(TodoApplication.APP_NAME);
////    }
//}
