package io.skysail.server.um.simple.authorization;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
public class SimpleUser {

    private String id;
    private String username;
    private String password;

    public SimpleUser(String id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    @Setter
    private Set<String> roles = new HashSet<>();
}
