package io.skysail.server.um.simple.usermanager;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserManagementRepository {

    private Map<String, String> usernamesAndPasswords = new HashMap<>();
    private Map<String, Set<String>> usernamesAndRoles = new HashMap<>();

    public UserManagementRepository(Map<String, String> config) {
        String usersDefinition = config.get("users");
        if (usersDefinition == null || usersDefinition.length() == 0) {
            log.warn("no users are defined");
            return;
        }
        createUsernamesAndPasswords(usersDefinition, config);
    }

    public Map<String, String> getUsernamesAndPasswords() {
        return Collections.unmodifiableMap(usernamesAndPasswords);
    }

    public Map<String, Set<String>> getUsernamesAndRoles() {
        return Collections.unmodifiableMap(usernamesAndRoles);
    }

    private void createUsernamesAndPasswords(String usersDefinition, Map<String, String> config) {

        Arrays.stream(usersDefinition.split(",")).map(u -> {
            return u.trim();
        }).forEach(username -> {
            String password = config.get(username + ".password");
            if (password != null) {
                usernamesAndPasswords.put(username, password);
            }
            ;
            String rolesDefinition = config.get(username + ".roles");
            if (rolesDefinition != null) {
                Set<String> roles = Arrays.stream(rolesDefinition.split(",")).map(r -> {
                    return r.trim();
                }).collect(Collectors.toSet());
                usernamesAndRoles.put(username, roles);
            }
        });
    }
}
