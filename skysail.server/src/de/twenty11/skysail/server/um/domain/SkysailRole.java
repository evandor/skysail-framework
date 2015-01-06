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

import javax.validation.constraints.Size;

public class SkysailRole {

    // @Field
    @Size(min = 3, message = "name must have at least three characters")
    private String name;

    public SkysailRole() {
    }

    public SkysailRole(String roleName) {
        this.name = roleName;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
