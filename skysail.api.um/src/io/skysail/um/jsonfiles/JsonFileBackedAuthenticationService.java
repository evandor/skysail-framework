package io.skysail.um.jsonfiles;

import io.skysail.um.api.AuthenticationService;

import java.util.List;

import org.restlet.Request;
import org.restlet.Response;

public class JsonFileBackedAuthenticationService implements AuthenticationService {

    private List<User> users;

    public JsonFileBackedAuthenticationService(List<User> users) {
        this.users = users;
    }

    @Override
    public void logout() {
    }

    @Override
    public void clearCache(String username) {
    }

    @Override
    public boolean authenticate(Request request, Response response) {
        String identifier = getIdentifier(request, response);
        char[] secret = getSecret(request, response);
        return users.stream().filter(u -> {
            return identifier.equals(u.getUsername()) && secret.equals(u.getPassword());
        }).findFirst().isPresent();
    }

    private String getIdentifier(Request request, Response response) {
        return request.getChallengeResponse().getIdentifier();
    }

    private char[] getSecret(Request request, Response response) {
        return request.getChallengeResponse().getSecret();
    }

}
