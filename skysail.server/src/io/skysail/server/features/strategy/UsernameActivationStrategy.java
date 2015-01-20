package io.skysail.server.features.strategy;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import de.twenty11.skysail.api.features.ActivationStrategy;
import de.twenty11.skysail.api.features.FeatureState;
import de.twenty11.skysail.api.um.SkysailUser;

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
    public boolean isActive(FeatureState state, SkysailUser user) {
        String usersAsString = state.getConfig().get(PARAM_USERS);
        if (StringUtils.isBlank(usersAsString)) {
            return false;
        }
        List<String> users = splitAndTrim(usersAsString);
        if (user == null || StringUtils.isBlank(user.getUsername())) {
            return false;
        }
        return users.stream().filter(u -> {
            return u.equals(user.getUsername());
        }).findFirst().isPresent();
    }

    private List<String> splitAndTrim(String str) {
        return Arrays.stream(str.split(",")).map(s -> s.trim()).collect(Collectors.toList());
    }
}
