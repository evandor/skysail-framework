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
 * A response indicating an error during the request.
 */
public class FailureResponse<T> extends SkysailResponse<T> {

    /**
     * Constructor taking a message.
     * 
     * @param message
     *            the provided message
     */
    public FailureResponse(final String message) {
        super(null);
    }

    /**
     * make sure to handle the exception properly (logging etc) before calling this method - this will only create a
     * response to the caller with the errors message, and no more details.
     * 
     * @param e
     *            the passed exception TODO for 0.2.0 let method accept string instead of exception
     */
    public FailureResponse(final Exception e) {
        this(e.getMessage() != null ? e.getMessage() : "Generic Error: Maybe NullPointerException?");
    }

}
