//package io.skysail.server.app.todos;
//
//import static org.hamcrest.Matchers.is;
//import static org.junit.Assert.assertThat;
//import net.thucydides.core.annotations.Step;
//
//public class TravellerSteps {
//
//    FrequentFlyer frequentFlyer  = new FrequentFlyer();
//
//    @Step("Given a traveller has a frequent flyer account with {0} points")
//    public void a_traveller_has_a_frequent_flyer_account_with_balance(int initialBalance) {
//        frequentFlyer.withInitialBalanceOf(initialBalance);
//    }
//
//    @Step("When the traveller flies {0} km")
//    public void the_traveller_flies(int distance) {
//        frequentFlyer.flies(distance);//.kilometers();
//
//    }
//
//    @Step("Then the traveller should have a balance of {0} points")
//    public void traveller_should_have_a_balance_of(int expectedBalance ) {
//       assertThat(frequentFlyer.getBalance(), is(expectedBalance));
//    }
//
//    @Step
//    public void a_traveller_joins_the_frequent_flyer_program() {
//         frequentFlyer.withInitialBalanceOf(0);
//    }
//
////    @Step
////    public void traveller_should_have_a_status_of(Status expectedStatus) {
////        assertThat(frequentFlyer.getStatus()).isEqualTo(expectedStatus);
////    }
//}
