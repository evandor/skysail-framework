/**
 *  Copyright 2011 Carsten Graef
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package de.twenty11.skysail.api.responses;

/**
 * A skysail server installation responds to (RESTful) Http-Requests by creating
 * responses, which get converted on-the-fly into the desired target format
 * (like html, json, xml, csv, ...).
 *
 * The actual data contained in the response is described by the generic type
 * parameter <T>; there are no formal restrictions on that type, but you have to
 * keep in mind that it is supposed to be serializable into formats as JSON, XML
 * and the like.
 *
 */
public class SkysailResponse<T> {

    protected T entity;

    public SkysailResponse(T entity) {
        this.entity = entity;
    }

    public SkysailResponse() {
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getSimpleName()).append(": ");
        return sb.toString();
    }

    public boolean isForm() {
        return (this instanceof FormResponse || this instanceof ConstraintViolationsResponse);
    }

    public T getEntity() {
        return entity;
    }
}
