//package io.skysail.server.app.todos.lists.test;
//
//import io.skysail.api.responses.SkysailResponse;
//import io.skysail.server.app.todos.TodoList;
//import net.thucydides.core.annotations.Step;
//
//import org.restlet.data.Form;
//
//public class ListSteps extends AbstractListResourceTest  {
//
//    @Step("Given a traveller has a frequent flyer account with {0} points")
//    public void a_traveller_has_a_frequent_flyer_account_with_balance(int initialBalance) {
//       // frequentFlyer.withInitialBalanceOf(initialBalance);
//    }
//
//    @Step("xxx_posting")
//    public void when_posting_this(String key, String val) {
//        Form form = new Form();
//        form.add(key, val);
//        SkysailResponse<TodoList> result = postListresource.post(form, HTML_VARIANT);
//        assertListResult(postListresource, result, "list1");
//    }
//}
