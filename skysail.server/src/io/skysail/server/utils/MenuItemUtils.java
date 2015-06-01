package io.skysail.server.utils;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.shiro.SecurityUtils;
import org.restlet.Request;
import org.restlet.security.Role;

import de.twenty11.skysail.server.services.*;
import de.twenty11.skysail.server.services.MenuItem.Category;

public class MenuItemUtils {

    public static Set<MenuItem> getMenuItems(Set<MenuItemProvider> menuProviders, Request request, Category category) {
        return menuProviders.stream() //
                .map(provider -> provider.getMenuEntries()) //
                .filter(menuEntries -> (menuEntries != null)) //
                .flatMap(entries -> entries.stream()) //
                .collect(Collectors.toSet()) //
                .stream() //
                .filter(item -> (item.getCategory().equals(category))) //
                .sorted((a, b) -> {
                    return b.getName().compareTo(a.getName());
                }).filter(item -> isAuthorized(item, request)).collect(Collectors.toSet());
    }

    private static boolean isAuthorized(MenuItem item, Request request) {
        boolean authenticated = SecurityUtils.getSubject().isAuthenticated();
        List<Role> clientRoles = request.getClientInfo().getRoles();
        if (!item.getNeedsAuthentication()) {
            return true;
        }
        List<String> clienRoleNames = clientRoles.stream().map(cr -> cr.getName()).collect(Collectors.toList());
        if (item.getSecuredByRole() != null) {
            if (item.getSecuredByRole().apply(clienRoleNames.toArray(new String[clienRoleNames.size()]))) {
                return true;
            }
        } else {
            if (authenticated) {
                return true;
            }
        }
        return false;
    }
}
