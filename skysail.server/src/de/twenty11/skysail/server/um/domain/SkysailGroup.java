/**
 * Copyright 2013 Carsten Graef
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

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import de.twenty11.skysail.api.forms.Field;

@Entity
@Table(name = "um_groups")
// @NamedQuery(name = "findByName", query = "SELECT c FROM SkysailUser c WHERE c.username = :name")
public class SkysailGroup implements Serializable {

    private static final long serialVersionUID = -3030387756527785881L;

    public static final String KEY_PRIVATE_GROUP = "1F1078B5-8B54-4E23-A085-863300CE7C81";
    public static final String KEY_PUBLIC_GROUP = "2D1C1342-49D3-4FB5-9517-36178F473012";
    public static final String KEY_WORLD_GROUP = "33C3F791-05FA-44E3-924F-91E2E1E14537";

    @Id
    @GeneratedValue(generator = "UM_GROUP_ID_GEN")
    private String id;

    @Size(min = 1, message = "name must not be empty")
    @Field
    private String name;

    @Field
    private String permissions = "0700";

    @Field
    private String description;

    // @ManyToMany
    // @JoinTable(name = "um_users_um_roles", joinColumns = { @JoinColumn(name = "SkysailUser_ID", referencedColumnName
    // = "ID") }, inverseJoinColumns = { @JoinColumn(name = "roles_ID", referencedColumnName = "ID") })
    // private List<SkysailRole> roles = new ArrayList<SkysailRole>();

    public SkysailGroup() {
        // roles.add(new SkysailRole("test"));
    }

    // public SkysailGroup(String username, String password) {
    // this.name = username;
    // this.permissions = password;
    // }

    public SkysailGroup(String name, String permissions) {
        this.name = name;
        this.permissions = permissions;
    }

    public String getId() {
        return this.id;
    }

    public void setId(final String userId) {
        this.id = userId;
    }

    public String getName() {
        return name;
    }

    public String getPermissions() {
        return permissions;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // public List<SkysailRole> getRoles() {
    // return roles;
    // }
    //
    // public void setRoles(List<SkysailRole> roles) {
    // this.roles = roles;
    // }
    //
    // @Override
    // public String toString() {
    // return name + "[" + roles.toString() + "]";
    // }
}
