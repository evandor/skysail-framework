package io.skysail.server.um.simple.usermanager;

import io.skysail.server.um.simple.authorization.SimpleUser;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

/**
 * A repository for users, passwords and roles, created in memory and
 * initialized with data from the configuration admin, that is, from a
 * configuration file.
 *
 */
@Slf4j
public class UserManagementRepository {

    private volatile Map<String, SimpleUser> users = new ConcurrentHashMap<>();

    public UserManagementRepository(Map<String, String> config) {
        String usersDefinition = config.get("users");
        if (usersDefinition == null || usersDefinition.length() == 0) {
            log.warn("no users are defined");
            return;
        }
        createUsers(usersDefinition, config);
    }

    public SimpleUser getByPrincipal(String principal) {
        return users.get(principal);
    }

    public SimpleUser getByUsername(String username) {
        Optional<SimpleUser> optionalUser = users.values().stream().filter(u -> {
            return u.getUsername().equals(username);
        }).findFirst();
        return optionalUser.orElse(null);
    }

    private void createUsers(String usersDefinition, Map<String, String> config) {

        Arrays.stream(usersDefinition.split(",")).map(u -> {
            return u.trim();
        }).forEach(username -> {
            String password = config.get(username + ".password");
            String id = config.get(username + ".id");
            if (id == null) {
                throw new IllegalStateException("could not find ID for user " + username);
            }
            SimpleUser simpleUser = null;
            if (password != null && id != null) {
                simpleUser = new SimpleUser(id, username, password);
                users.put(id, simpleUser);
            }
            ;
            String rolesDefinition = config.get(username + ".roles");
            if (rolesDefinition != null && simpleUser != null) {
                Set<String> roles = Arrays.stream(rolesDefinition.split(",")).map(r -> {
                    return r.trim();
                }).collect(Collectors.toSet());
                simpleUser.setRoles(roles);
            }
        });
    }

}
