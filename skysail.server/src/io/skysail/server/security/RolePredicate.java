package io.skysail.server.security;

import java.util.Arrays;

import com.google.common.base.Predicate;

public class RolePredicate implements Predicate<String[]> {

    private String requiredRole;

    /**
     * A predicate which will return true if the required role is contained in the provided roles.
     * 
     * @param requiredRole the role to be checked. If null, the predicate will always return true.
     */
    public RolePredicate(String requiredRole) {
        this.requiredRole = requiredRole;
    }

    @Override
    public boolean apply(String[] providedRoles) {
        if (requiredRole == null) {
            return true;
        }
        return Arrays.asList(providedRoles).contains(requiredRole);
    }
    
    @Override
    public String toString() {
        return new StringBuilder("required role: '").append(requiredRole).append("'").toString();
    }

}