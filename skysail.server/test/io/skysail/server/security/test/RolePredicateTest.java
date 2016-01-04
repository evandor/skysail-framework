package io.skysail.server.security.test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import io.skysail.server.security.RolePredicate;

public class RolePredicateTest {

    private String myRole;
    private String yourRole;

    @Before
    public void setUp() {
        myRole = "myRole"; //new Role("myRole");
        yourRole = "yourRole"; //new Role("yourRole");
    }
    @Test
    public void returns_true_if_the_required_role_is_null() throws Exception {
        RolePredicate rolePredicate = new RolePredicate(null);
        assertThat(rolePredicate.apply(null), is(true));
    }

    @Test
    public void returns_true_if_the_required_role_is_contained_in_the_provided_roles() throws Exception {
        RolePredicate rolePredicate = new RolePredicate(myRole);
        assertThat(rolePredicate.apply(new String[]{ "myRole","yourRole" }), is(true));
    }

    @Test
    public void returns_false_if_the_required_role_is_not_contained_in_the_provided_roles() throws Exception {
        RolePredicate rolePredicate = new RolePredicate(myRole);
        assertThat(rolePredicate.apply(new String[]{ "yourRole" }), is(false));
    }

}
