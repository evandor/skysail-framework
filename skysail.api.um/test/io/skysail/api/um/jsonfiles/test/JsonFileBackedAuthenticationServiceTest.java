//package io.skysail.api.um.jsonfiles.test;
//
//import io.skysail.api.um.User;
//import io.skysail.api.um.jsonfiles.JsonFileBackedAuthenticationService;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mockito;
//import org.restlet.Request;
//import org.restlet.Response;
//import org.restlet.data.ChallengeResponse;
//import org.restlet.data.ChallengeScheme;
//
//public class JsonFileBackedAuthenticationServiceTest {
//
//    private List<User> users = new ArrayList<>();
//    private JsonFileBackedAuthenticationService authenticationService;
//    private Request request;
//    private Response response;
//    private ChallengeResponse challengeResponse;
//
//    @Before
//    public void setUp() throws Exception {
//        users.add(new User("admin", "skysail"));
//        authenticationService = new JsonFileBackedAuthenticationService(users);
//
//        request = Mockito.mock(Request.class);
//        response = Mockito.mock(Response.class);
//
//        Mockito.when(response.getRequest()).thenReturn(request);
//
//    }
//
//    @Test
//    public void authenticates_existing_user_with_proper_password() throws Exception {
//
//        challengeResponse = new ChallengeResponse(ChallengeScheme.CUSTOM, "admin", "skysail");
//        Mockito.when(request.getChallengeResponse()).thenReturn(challengeResponse);
//
//        authenticationService.authenticate(request, response);
//    }
//
// }
