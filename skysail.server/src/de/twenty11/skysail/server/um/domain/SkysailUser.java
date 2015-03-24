/**
 * Copyright 2011 Carsten GrGraeff
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 */
package de.twenty11.skysail.server.um.domain;

import io.skysail.api.forms.Field;
import io.skysail.api.forms.InputType;

import java.util.ArrayList;
import java.util.List;

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

    @Field(type = InputType.PASSWORD)
    private String password;

    private String salt;

    private List<SkysailRole> roles = new ArrayList<SkysailRole>();

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

    public List<SkysailRole> getRoles() {
        return roles;
    }

    public void setRoles(List<SkysailRole> roles) {
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

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getSalt() {
        return salt;
    }

    public Object getRid() {
        return rid;
    }

    public void setRid(Object rid) {
        this.rid = rid;
    }
}
