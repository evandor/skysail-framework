package de.twenty11.skysail.server.um.domain;

import io.skysail.api.forms.Field;
import io.skysail.api.forms.InputType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Id;
import javax.persistence.ManyToMany;

public class SkysailUser {

    /**
     * this username is reserved; altering any data should be prevented by the
     * system.
     */
    public static final String SYSTEM_USER = "system";

    @Id
    private Object rid;

    @Field
    private String username;

    @Field(inputType = InputType.PASSWORD)
    private String password;

    private Set<SkysailRole> roles = new HashSet<SkysailRole>();

    @ManyToMany
    private List<SkysailGroup> groups = new ArrayList<SkysailGroup>();

    public SkysailUser() {
        // roles.add(new SkysailRole("test"));
    }

    public SkysailUser(String username, String password, String id) {
        this.username = username;
        this.password = password;
        this.rid = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<SkysailRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<SkysailRole> roles) {
        this.roles = roles;
    }

    public List<SkysailGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<SkysailGroup> groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        return username + "[" + roles.toString() + "]";
    }

    public Object getRid() {
        return rid;
    }

    public void setRid(Object rid) {
        this.rid = rid;
    }
}
