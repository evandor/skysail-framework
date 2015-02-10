package io.skysail.server.features.strategy;

import io.skysail.api.features.ActivationStrategy;
import io.skysail.api.features.FeatureState;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

public class UsernameActivationStrategy implements ActivationStrategy {

    public static final String ID = "username";

    public static final String PARAM_USERS = "users";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "Users by name";
    }

    @Override
    public boolean isActive(FeatureState state, String username) {
        String usersAsString = state.getConfig().get(PARAM_USERS);
        if (StringUtils.isBlank(usersAsString)) {
            return false;
        }
        List<String> users = splitAndTrim(usersAsString);
        if (StringUtils.isBlank(username)) {
            return false;
        }
        return users.stream().filter(u -> {
            return u.equals(username);
        }).findFirst().isPresent();
    }

    private List<String> splitAndTrim(String str) {
        return Arrays.stream(str.split(",")).map(s -> s.trim()).collect(Collectors.toList());
    }
}
