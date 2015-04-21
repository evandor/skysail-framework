//package de.twenty11.skysail.server.ext.mail.accounts.impl;
//
//import static org.hamcrest.Matchers.equalTo;
//import static org.hamcrest.Matchers.is;
//import static org.junit.Assert.assertThat;
//
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ConcurrentMap;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mockito;
//import org.mockito.invocation.InvocationOnMock;
//import org.mockito.stubbing.Answer;
//import org.restlet.Request;
//import org.restlet.data.Reference;
//
//import de.twenty11.skysail.api.responses.SkysailResponse;
//import de.twenty11.skysail.server.ext.mail.MailApplication;
//import de.twenty11.skysail.server.ext.mail.accounts.Account;
//import de.twenty11.skysail.server.ext.mail.accounts.impl.AccountRepository;
//import de.twenty11.skysail.server.ext.mail.accounts.impl.AccountResource;
//import de.twenty11.skysail.server.ext.mail.accounts.impl.InMemoryAccountRepository;
//import de.twenty11.skysail.server.validation.ResourceTestWithUnguardedAppication;
//
//public class AccountResourceTest extends ResourceTestWithUnguardedAppication<MailApplication> {
//
//    private AccountResource accountResource;
//    private MailApplication spy;
//    private final AccountRepository accountRepository = new InMemoryAccountRepository();
//
//    @Before
//    public void setUp() throws Exception {
//        spy = (MailApplication) setUpMockedApplication(new MailApplication());
//        setupRepository();
//        accountResource = new AccountResource();
//    }
//
//    private void setupRepository() {
//        Mockito.doAnswer(new Answer<AccountRepository>() {
//            @Override
//            public AccountRepository answer(InvocationOnMock invocation) throws Throwable {
//                return accountRepository;
//            }
//        }).when(spy).getAccountRepository();
//    }
//
//    @Test
//    public void add_account_successfully() {
//        SkysailResponse<?> response = accountResource.addEntity(new Account("host", "user", "pass"));
//        assertThat(response.getSuccess(), is(true));
//    }
//
//    @Test
//    public void added_account_can_be_retrieved_again() {
//        Account account = new Account("host", "user", "pass");
//        accountResource.addEntity(account);
//
//        // TODO refactor out
//        Request request = Mockito.mock(Request.class);
//        ConcurrentMap<String, Object> attributes = new ConcurrentHashMap<String, Object>();
//        attributes.put("id", account.getId());
//        Mockito.when(request.getAttributes()).thenReturn(attributes);
//        Reference resourceRef = Mockito.mock(Reference.class);
//        Mockito.when(request.getResourceRef()).thenReturn(resourceRef);
//        accountResource.init(null, request, null);
//        SkysailResponse<Account> response = accountResource.getEntity();
//        Account retrievedAccount = response.getData();
//
//        assertThat(retrievedAccount.getHost(), is(equalTo("host")));
//        assertThat(retrievedAccount.getUser(), is(equalTo("user")));
//        assertThat(retrievedAccount.getPass(), is(equalTo("pass")));
//    }
//
// }
